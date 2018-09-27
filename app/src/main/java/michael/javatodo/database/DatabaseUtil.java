package michael.javatodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import michael.javatodo.Todo;

import java.util.ArrayList;

public class DatabaseUtil {

    private Context ctx;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseUtil(Context ctx) {
        this.ctx = ctx;

        helper = new DatabaseHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public ArrayList<Todo> getTodos() {
        ArrayList<Todo> todos = new ArrayList<Todo>();

        String[] fields = new String[2];
        fields[0] = "_id";
        fields[1] = "text";
        fields[2] = "done";

        Cursor cursor = db.query("todos", fields, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String text = cursor.getString(1);
            Boolean done = false;

            int doneInt = cursor.getInt(2);
            if (doneInt == 0) {
                done = false;
            } else if (doneInt == 1) {
                done = true;
            } else {
                done = false;
                Log.d("DBUtil", "done not 0 or 1!");
            }

            Todo t = new Todo(text, done, id);
            todos.add(t);
        }

        return todos;
    }

    public Long addTodo(Todo t) {
        ContentValues cv = new ContentValues();
        int done = 0;
        if(t.done) {
            done = 1;
        }

        cv.put("text", t.text);
        cv.put("done", done);

        return db.insert("todos", null, cv);
    }

    public void remoteTodo(Todo t) {
        String[] value = new String[0];
        value[0] = t.id.toString();

        db.delete("todos", "'_id' = ?", value);
    }

    public void nukeDB() {
        helper.nuke(db);
    }

    public void check() {
        helper.safetyCheck(db);
    }
}

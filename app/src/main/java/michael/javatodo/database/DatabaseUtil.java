package michael.javatodo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import michael.javatodo.Todo;

import java.util.ArrayList;

public class DatabaseUtil {

    private Context ctx;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public DatabaseUtil(Context ctx) {
        // Store passed context for future use
        // (I don't think this is needed, todo check this out)
        this.ctx = ctx;

        // Create a new DatabaseHelper
        // then get a writable DB instance
        helper = new DatabaseHelper(ctx);
        db = helper.getWritableDatabase();
    }

    public ArrayList<Todo> getTodos() {
        // Create empty todos array
        ArrayList<Todo> todos = new ArrayList<>();

        // Create array of the fields we need to pull
        String[] fields = new String[]{"_id", "text", "done"};

        // Run the query
        Cursor cursor = db.query("todos", fields, null, null, null, null, null);

        // Loop over each of the returned rows
        while (cursor.moveToNext()) {
            // Get id and text since they don't require "conversion"
            int id = cursor.getInt(0);
            String text = cursor.getString(1);
            Boolean done;

            // Get "done" which is 0 (false) or 1 (done)
            int doneInt = cursor.getInt(2);
            // Depending on value, we set the Boolean done to true or false
            if (doneInt == 0) {
                done = false;
            } else if (doneInt == 1) {
                done = true;
            } else {
                done = false;
                Log.d("DBUtil", "done not 0 or 1!");
            }

            // Create a new object with the pulled data
            Todo t = new Todo(text, done, id);
            // Add it to the list to be returned
            todos.add(t);
        }

        // Since we're done in the loop, return the array
        return todos;
    }

    // Helper function to add a item to the db
    public Long addTodo(Todo t) {
        // Used to create insert query
        ContentValues cv = new ContentValues();
        // Translate done boolean to int
        int done = 0;
        if(t.done) {
            done = 1;
        }

        // Put the values in the ContentValues
        cv.put("text", t.text);
        cv.put("done", done);

        // Run the insert and return it's output (Long id of new row)
        return db.insert("todos", null, cv);
    }

    // Helper function to remove a item from the db
    public void remoteTodo(Todo t) {
        // Used as values for delete query
        String[] args = new String[]{ String.valueOf(t.id) };

        Log.d("DBUtil", String.format("deleting todo with id %d", t.id));
        // Run delete
        int affectedRows = db.delete("todos", "_id = ?", args);
        Log.d("DBUtil", String.format("%d rows affected", affectedRows));
    }

    // Helper function to nuke db
    // (calls helper's nuke function)
    public void nukeDB() {
        helper.nuke(db);
    }

    // Helper function to run safety check
    // (calls helper's safetyCheck function)
    public void check() {
        helper.safetyCheck(db);
    }
}

package michael.javatodo.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    String CREATE_DB = "CREATE TABLE IF NOT EXISTS `todos` (\n" +
            " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            " `text` TEXT NOT NULL,\n" +
            " `done` INTEGER NOT NULL DEFAULT 0\n" +
            ");";

    public DatabaseHelper(Context ctx) {
        super(ctx, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate()");
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", String.format("onUpgrade() %d -> %d", oldVersion, newVersion));
        db.execSQL("DROP TABLE IF EXISTS 'todos'");
        onCreate(db);
    }

    public void nuke(SQLiteDatabase db) {
        Log.d("DBHelper", "Nuking db...");
        db.execSQL("DROP TABLE IF EXISTS 'todos'");
        onCreate(db);
    }

    public void safetyCheck(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }
}

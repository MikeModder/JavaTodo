package michael.javatodo.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // SQL to create our table
    private String CREATE_DB = "CREATE TABLE IF NOT EXISTS `todos` (\n" +
            " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            " `text` TEXT NOT NULL,\n" +
            " `done` INTEGER NOT NULL DEFAULT 0\n" +
            ");";

    // Constructor which sets up the db
    public DatabaseHelper(Context ctx) {
        super(ctx, "TodoDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate()");
        // Create the db using the CREATE_DB SQL from above
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHelper", String.format("onUpgrade() %d -> %d", oldVersion, newVersion));
        // Since this is a simple app, simply drop table and remake it
        // Ideally, you would handle upgrades better and move all the data
        // todo ^
        db.execSQL("DROP TABLE IF EXISTS 'todos'");
        // Call onCreate() to recreate the table
        onCreate(db);
    }

    public void nuke(SQLiteDatabase db) {
        Log.d("DBHelper", "Nuking db...");

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS 'todos'");
        // Call onCreate() to recreate the table
        onCreate(db);
    }

    // Run the creation SQL
    // (if the table already exists, this does nothing.
    //  under normal circumstances, this should never actually
    //  be needed, but I have it here just in case I break something)
    public void safetyCheck(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }
}

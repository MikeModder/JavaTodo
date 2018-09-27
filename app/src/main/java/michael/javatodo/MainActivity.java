package michael.javatodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import michael.javatodo.database.DatabaseUtil;

import java.util.ArrayList;

// Code written by Michael
// Java port of my Kotlin todo app
// https://github.com/MikeModder/KotlinTodo
// With some extra enhancements, namely fixing a
// little bug that broke deleting todos.
// Both versions are missing the ability to
// mark todos oa "done" using a checkbox on
// the todo's row. Additionally, long items
// will likely be cut off as the text box
// won't automatically expand to fit it's
// content.

public class MainActivity extends AppCompatActivity {

    /*
        todos - Store the list of todos here for easy usage
        todoAdapter - Adapter which allows me to make a nice list
        lvTodods - the ListView containing all todos
        DButil - Local instance of 'DBUtil' helper class
     */
    ArrayList<Todo> todos;
    ArrayAdapter<Todo> todoAdapter;
    ListView lvTodos;
    DatabaseUtil DButil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create instance and show main layout
        // (currently the only layout)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fire up DatabaseUtil using the current application context
        DButil = new DatabaseUtil(super.getApplicationContext());
        // Run a check to make sure he have the 'todos' table
        DButil.check();

        // Find the ListView
        lvTodos = findViewById(R.id.lvTodos);
        // Pull all todos from the database
        todos = DButil.getTodos();
        // Create the adapter
        todoAdapter = new TodoAdapter(super.getApplicationContext(), todos);

        // Use the adapter for our ListView
        lvTodos.setAdapter(todoAdapter);
        // Add a long click listener
        lvTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get item at position i
                Todo deleteTodo = todoAdapter.getItem(position);
                // Use DButil to remove it from the db
                DButil.remoteTodo(deleteTodo);
                // Remove it from the ListView
                // (This is slightly hacky, but it saves having to call redoList())
                todoAdapter.remove(deleteTodo);
                return true;
            }
        });

    }

    // Function ran when add item button is clicked
    public void addListItem(View v) {
        // Get the EditText
        EditText inputBox = findViewById(R.id.inputBox);
        // Check if it's empty (do nothing) or "db::nuke (reset db)
        if(inputBox.getText().toString() == "") return;
        if(inputBox.getText().toString() == "db::nuke") {
            DButil.nukeDB();
            redoList();
            return;
        }

        // Create a new Todo using the user's input
        // Mark it as not done
        Todo newTodo = new Todo(inputBox.getText().toString(), false, -1);
        // Add it to the database
        DButil.addTodo(newTodo);
        // Redraw list
        redoList();

        // Clear input
        inputBox.getText().clear();
    }

    public void redoList() {
        // Clear the ListView and re-add stuff from the DB
        // Not the best way of doing this, as every change will cause the list to be redone
        // However, this is my lazy fix to removing todos easily

        // Remove all items from the ListView
        todoAdapter.clear();
        // And then repopulate it with entries from the db
        todoAdapter.addAll(DButil.getTodos());
    }
}

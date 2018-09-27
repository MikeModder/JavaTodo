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

public class MainActivity extends AppCompatActivity {

    ArrayList<Todo> todos;
    ArrayAdapter<Todo> todoAdapter;
    ListView lvTodos;
    DatabaseUtil DButil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DButil = new DatabaseUtil(super.getApplicationContext());
        DButil.check();

        lvTodos = findViewById(R.id.lvTodos);
        todos = DButil.getTodos();
        todoAdapter = new TodoAdapter(super.getApplicationContext(), todos);

        lvTodos.setAdapter(todoAdapter);
        lvTodos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Todo deleteTodo = todoAdapter.getItem(position);
                DButil.remoteTodo(deleteTodo);
                todoAdapter.remove(deleteTodo);
                return true;
            }
        });

    }

    public void addListItem(View v) {
        EditText inputBox = findViewById(R.id.inputBox);
        if (inputBox.getText().toString() == "") return;
        if(inputBox.getText().toString() == "db::nuke") {
            DButil.nukeDB();
            redoList();
            return;
        }

        Todo newTodo = new Todo(inputBox.getText().toString(), false, -1);
        DButil.addTodo(newTodo);
        redoList();

        inputBox.getText().clear();
    }

    public void redoList() {
        // Clear the ListView and re-add stuff from the DB
        // Not the best way of doing this, as every change will cause the list to be redone
        // However, this is my lazy fix to removing todos easily
        todoAdapter.clear();
        todoAdapter.addAll(DButil.getTodos());
    }
}

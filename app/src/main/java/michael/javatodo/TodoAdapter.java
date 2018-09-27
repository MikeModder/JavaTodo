package michael.javatodo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoAdapter extends ArrayAdapter<Todo> {
    private Context ctx;
    private ArrayList<Todo> todos;

    public TodoAdapter(Context ctx, ArrayList<Todo> todos) {
        super(ctx, 0, todos);

        this.ctx = ctx;
        this.todos = todos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Todo t = getItem(position);

        if(convertView == null) {
            View convertViewB;
            convertViewB = LayoutInflater.from(ctx).inflate(R.layout.todo_row, parent, false);
            TextView tvText = convertViewB.findViewById(R.id.todoText);
            //CheckBox isDone = convertViewB.findViewById(R.id.isDone);

            tvText.setText(t.text);
            //isDone.setChecked(!t.done);

            return  convertViewB;
        }

        convertView = LayoutInflater.from(ctx).inflate(R.layout.todo_row, parent, false);
        TextView tvText = convertView.findViewById(R.id.todoText);
        //CheckBox isDone = convertView.findViewById(R.id.isDone);

        tvText.setText(t.text);
        //isDone.setChecked(!t.done);

        return  convertView;
    }
}

package michael.javatodo;

public class Todo {
    public String text;
    public Boolean done;
    public Integer id;

    public Todo(String text, Boolean done, Integer id) {
        this.done = done;
        this.id = id;
        this.text = text;
    }

}

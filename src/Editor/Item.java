package Editor;

/**
 * Created by Thomas VENNER on 26/12/2016.
 */
public class Item {
    private String text;
    private Runnable method;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Runnable getMethod() {
        return method;
    }

    public void setMethod(Runnable method) {
        this.method = method;
    }

    public Item(String text, Runnable method) {
        this.text = text;
        this.method = method;
    }

    public void runMethod() {
        method.run();
    }
}

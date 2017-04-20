package baltamon.mx.appgooglemaps.models;

/**
 * Created by Baltazar Rodriguez on 19/04/2017.
 */

public class Duration {
    private String text; //Representation of the time
    private int value; //Exact time in seconds

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

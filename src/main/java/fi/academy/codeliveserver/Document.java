package fi.academy.codeliveserver;

/**
 * Created by Jari Haavisto
 */
public class Document {

    private StringBuilder text;
    private String filename;

    public Document() {
        this.text = new StringBuilder();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String text) {
        this.text = new StringBuilder(text);
    }

    public void delete(int start, int end) {
        text.delete(start, end);
    }

    public void insert(int offset, String insertText) {
        text.insert(offset, insertText);
    }

    public void insert(int start, int end, String insertText) {
        delete(start, end);
        insert(start, insertText);

    }
}



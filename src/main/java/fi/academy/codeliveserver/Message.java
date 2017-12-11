package fi.academy.codeliveserver;

/**
 * Created by Administrator on 05/12/2017.
 */
public class Message {

    private String filename, content;
    private int startPos, endPos;
    private MessageType type;

    public enum MessageType {DELTA, FULL, NAME}

    public Message() {
    }

    public Message(String content) {
        this.content = content;
    }

    public Message(Document document) {
        this.filename = document.getFilename();
        this.content = document.getText();
        this.type = MessageType.FULL;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "Message{" +
                "filename='" + filename + '\'' +
                ", content='" + content + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", type=" + type +
                '}';
    }
}

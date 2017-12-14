package fi.academy.codeliveserver.message;

/**
 * Created by Jari Haavisto
 *
 * Message on websocketissa kulkevien viestien perusmuoto. Siihen kuuluu
 * viestin tyyppi ja sisältö. Sisällön luonne muuttuu tyypin mukaan.
 */


public class Message {

    private String content;
    private MessageType type;

    public enum MessageType {DELTA, FULL, NAME, JOIN, USERS}

    public Message() {
        this.content = "";
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

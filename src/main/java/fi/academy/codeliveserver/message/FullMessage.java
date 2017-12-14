package fi.academy.codeliveserver.message;

import fi.academy.codeliveserver.message.Message;

/**
 * Created by Administrator on 11/12/2017.
 */
public class FullMessage extends Message {
    private String filename;

    public FullMessage() {
        this.filename = "";
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

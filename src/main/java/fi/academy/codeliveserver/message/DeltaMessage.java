package fi.academy.codeliveserver.message;

import fi.academy.codeliveserver.message.Message;

/**
 * Created by Administrator on 11/12/2017.
 */
public class DeltaMessage extends Message {

    private int startPos, endPos;

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

}

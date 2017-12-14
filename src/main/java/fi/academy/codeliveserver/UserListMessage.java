package fi.academy.codeliveserver;

import fi.academy.codeliveserver.message.Message;

import java.util.List;

/**
 * Created by Administrator on 12/12/2017.
 */
public class UserListMessage extends Message {
    List<String> users;

    public UserListMessage() {
    }

    public UserListMessage(List<String> users) {
        super.setType(MessageType.USERS);
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

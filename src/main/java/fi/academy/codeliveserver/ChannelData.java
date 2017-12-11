package fi.academy.codeliveserver;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jari Haavisto
 */
@Component("channelData")
public class ChannelData {
    Map<String, Document> documents;
    Map<String, Integer> users;

    public ChannelData() {
        this.documents = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void subscribe(String channel) {
        if (!documents.containsKey(channel)) {
            documents.put(channel, new Document());
            users.put(channel, 1);
        } else {
            users.put(channel, users.get(channel) + 1);
        }
    }

    public boolean contains(String key) {
        return documents.containsKey(key);
    }

    public void put(String key, Document value) {
        documents.put(key, value);
    }

    public Document get(String key) {
        return documents.get(key);
    }
}

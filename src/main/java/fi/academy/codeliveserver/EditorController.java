package fi.academy.codeliveserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jari Haavisto
 */
@Controller
public class EditorController {

    Map<String, Document> contents = new HashMap<>();
    Map<String, Integer> nOfUsers = new HashMap<>();

    @Autowired
    ChannelData channelData;

    @SubscribeMapping("/channel/{channelName}")
    public Message channelSubscription(@DestinationVariable String channelName) {
        channelData.subscribe(channelName);
        return new Message(channelData.get(channelName));
    }

    @MessageMapping("/send/{channelName}")
    @SendTo("/channel/{channelName}")
    public Message receiveMessage(@Payload Message message, @DestinationVariable("channelName") String channelName) {
        Document document = channelData.get(channelName);
        System.out.println("RECEIVED " + message + " @ " + channelName);
        switch (message.getType()) {
            case DELTA:
                document.insert(message.getStartPos(), message.getEndPos(), message.getContent());
                break;
            case NAME:
                document.setFilename(message.getFilename());
                break;
        }
        return message;
    }

}

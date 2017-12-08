package fi.academy.codeliveserver;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by Jari Haavisto
 */
@Controller
public class EditorController {

    Document document = new Document();

    @SubscribeMapping("/channel/public")
    public Message joinChannel() {
        Message message = new Message();
        message.setType(Message.MessageType.FULL);
        message.setContent(document.getText());
        message.setFilename(document.getFilename());
        return message;
    }

    @MessageMapping("/send")
    @SendTo("/channel/public")
    public Message message(Message message) {
        System.out.println("MESSAGE RECEIVED: " + message);
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

package fi.academy.codeliveserver;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by Jari Haavisto
 */
@Controller
public class EditorController {

    @MessageMapping("/send")
    @SendTo("/channel/public")
    public Message message(Message message) {
        return new Message("return message");
    }

}

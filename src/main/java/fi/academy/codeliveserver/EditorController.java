package fi.academy.codeliveserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Jari Haavisto
 */
@Controller
@EnableAutoConfiguration
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


    @Autowired
    private DocumentRepository tallennaTietokantaan;

    @RequestMapping("/tallenna")
    @ResponseBody
    public ResponseEntity tallennaTietokantaan() {
        tallennaTietokantaan.save(document);
        return ResponseEntity.ok("Tallennettu tietokantaan ID:llä X. Palaa takaisin <a href='http://codelive-client.herokuapp.com/'>etusivulle</a>)");
    }

}

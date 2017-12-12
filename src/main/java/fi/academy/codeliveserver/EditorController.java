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

    // MIKA: Koska pistokkeen eli WebSocketin konfiguraatiossa on määritelty ns. juuren murupoluksi "/ws",
    // vastaanottaa tämä Spring Kontrollerin metodi "send" murupolkuun "ws/send" saapuvat viestilähetykset
    // selaimen STOMP-protokollan asiakkaalta eli HTML:n index-sivulla JavaScriptissä
    // Stomp.over(new SockJS('/send'))
    @MessageMapping("/send")    // @MessageMapping("/viestit_sisaan")
    // Saapunut viesti käsitellään ja sen jälkeen se lähetetään tässä määriteltyyn osoitteeseen,
    @SendTo("/channel/public")  // @SendTo("/keskustelunaihe_ulos/viestisanoma")
    // josta kaikki keskusteluaiheen seuraajat (tilaajat) vastaanottavat lähetyksen
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

    // MIKA
    @RequestMapping("/tallenna")
    @ResponseBody
    public ResponseEntity tallennaTietokantaan() {
        int id = tallennaTietokantaan.save(document).getId();
        return ResponseEntity.ok("Tallennettu tietokantaan ID:llä " + id + ". Palaa takaisin <a href='http://codelive-client.herokuapp.com/'>etusivulle</a>)");
    }

}

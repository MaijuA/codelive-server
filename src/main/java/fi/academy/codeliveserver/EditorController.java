package fi.academy.codeliveserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
/*
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
*/

/**
 * Created by Jari Haavisto
 */
@Controller
@EnableAutoConfiguration
public class EditorController {

    @Autowired
    ChannelData channelData;

    /*
    Kun käyttäjä kirjautuu kanavalle, ajetaan metodi channelSubscription. Muuttujaan
    channelData talletetaan tieto siitä, että kanavalle on tullut yksi käyttäjä lisää.
    Käyttäjälle palautetaan viestissä teksti, joka kanavalle on jo kirjoitettu.
     */
    @SubscribeMapping("/channel/{channel}")
    public FullMessage channelSubscription(@DestinationVariable("channel") String channel) {
        Document channelContent = channelData.get(channel);
        FullMessage message = new FullMessage();
        message.setContent(channelContent.getText());
        message.setFilename(channelContent.getFilename()); /////  <--- tuolta null?
        message.setType(Message.MessageType.FULL);
        return message;
    }

    /*
    Koska käyttämämme palvelin (SimpleBroker) ei osaa käsitellä subscribe/unsubscribe-tapahtumia
    riittävällä detaljitasolla, asiakaspuolen odotetaan lähettävän viesti ennen tapahtumaa
    osoitteeseen "/send/{channel}.join" tai "/send/{channel}.leave". Lähetetyssä viestissä
    tulee olla sisältönä (content) käyttäjänimi. Metodit joinChannel ja leaveChannel käsittelevät
    toimenpiteet, jotka tulee tehdä käyttäjän liittyessä/poistuessa kanavalta.
     */
    @MessageMapping("/send/{channel}.join")
    @SendTo("/channel/{channel}")
    public UserListMessage joinChannel(@Payload Message message, @DestinationVariable("channel") String channel) {
        System.out.println("JOINING:" + message.getContent());
        channelData.subscribe(channel, message.getContent());
        return new UserListMessage(channelData.getUsers(channel));
    }

    @MessageMapping("/send/{channelName}.leave")
    public void leaveChannel(@Payload Message message, @DestinationVariable("channelName") String channelName) {
        System.out.println("LEAVING:" + message.getContent());
        channelData.unsubscribe(channelName, message.getContent());
    }


    /*
    Metodi filenameController ottaa vastaan viestit, jotka sisältävät tietoa tiedostonimen muutoksista
     */
    @MessageMapping("/filename/{channel}")
    @SendTo("/channel/{channel}")
    public Message filenameController(@Payload Message message, @DestinationVariable("channel") String channelName) {
        channelData.get(channelName).setFilename(message.getContent());
        return message;
    }

    /*
    Metodi deltaControlelr ottaa vastaan viestit, jotka sisältävät tietoa tekstimuutoksista.
     */
    @MessageMapping("/delta/{channel}")
    @SendTo("/channel/{channel}")
    public DeltaMessage deltaController(@Payload DeltaMessage message, @DestinationVariable("channel") String channel) {
        Document document = channelData.get(channel);
        document.insert(message.getStartPos(), message.getEndPos(), message.getContent());
        return message;
    }
/*
 *
 * Tämä ilmeisesti keskeneräinen, joten laitan kommentteihin. --Jari


    @Autowired
    private DocumentRepository tallennaTietokantaan;

    // MIKA
    @RequestMapping("/tallenna")
    @ResponseBody
    public ResponseEntity tallennaTietokantaan() {
        int id = tallennaTietokantaan.save(document).getId();
        return ResponseEntity.ok("Tallennettu tietokantaan ID:llä " + id + ". Palaa takaisin <a href='http://codelive-client.herokuapp.com/'>etusivulle</a>)");
    }
    */

}

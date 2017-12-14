package fi.academy.codeliveserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;


/**
 * Created by Jari Haavisto
 */
@Controller
@EnableAutoConfiguration // MIKA: Luo viestinvälittäjän ja mahdollistaa websocketien käytön
public class EditorController {

    @Autowired
    ChannelData channelData;

    /*
    JARI: Kun käyttäjä kirjautuu kanavalle, ajetaan metodi channelSubscription. Muuttujaan
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
    JARI: Koska käyttämämme palvelin (SimpleBroker) ei osaa käsitellä subscribe/unsubscribe-tapahtumia
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
    JARI: Metodi filenameController ottaa vastaan viestit, jotka sisältävät tietoa tiedostonimen muutoksista
     */
    @MessageMapping("/filename/{channel}")
    @SendTo("/channel/{channel}")
    public Message filenameController(@Payload Message message, @DestinationVariable("channel") String channelName) {
        channelData.get(channelName).setFilename(message.getContent());
        return message;
    }

    /*
    JARI: Metodi deltaControlelr ottaa vastaan viestit, jotka sisältävät tietoa tekstimuutoksista.
     */
    @MessageMapping("/delta/{channel}")
    @SendTo("/channel/{channel}")
    public DeltaMessage deltaController(@Payload DeltaMessage message, @DestinationVariable("channel") String channel) {
        Document document = channelData.get(channel);
        document.insert(message.getStartPos(), message.getEndPos(), message.getContent());
        return message;
    }

    @Autowired  // MIKA: Yhdistää DocumentRepository nimisen rajapinnan käsittelemään dataa
    // (kommunikoitaessa tietokannan kanssa) tämän java-luokan kautta
    private DocumentRepository tallennaTietokantaan;  // MIKA: Annetaan repolle alias

    // MIKA: Annotaatio @MessageMapping käyttää tässä WebSocketissa STOMP-protokollaa. Koska pistokkeen eli WebSocketin
    // konfiguraatiossa on määritelty ns. juuren murupoluksi "/ws", vastaanottaa tämä Spring Kontrollerin metodi
    // "send" murupolkuun "ws/send" saapuvat viestilähetykset selaimen STOMP-protokollan asiakkaalta
    // eli JavaScriptissä kohdassa Stomp.over(new SockJS('/send'))
    @MessageMapping("/send/{channel}.save") // @MessageMapping("/viestit_sisaan"); (@RequestMapping olisi ollut http-protokollalle)
    // Aaltosuluissa oleva "foo" {channel} on lähettävän asiakasselain-clientin sen hetkinen merkkijono osoiterivillä.
    // Lopussa oleva .save ei ole toiminnallinen vaan ryhmätyöskentelyä helpottava lisämääre nopeaan
    // koodin silmäilyyn (void / "tee jotain" tilamuutoksen seurannan sijaan).
    // Saapunut viesti käsitellään ja sen jälkeen se lähetetään tässä alla olevassa annotaatiossa määriteltyyn osoitteeseen
    @SendTo("/channel/{channel}")   // @SendTo("/keskustelunaihe_ulos/viestisanoma")
    // josta kaikki keskusteluaiheen seuraajat (tilaajat) vastaanottavat lähetyksen
    public void tallennaTietokantaan(@DestinationVariable("channel") String channel) { // Mikäli STOMP-protokollan
        // (Simple Text Oriented Message Protocol) yhteydenotossa haluttaisiin selain-clientiltä lähettää tietoa palvelimen
        // käsiteltäväksi tulisi yllä olevaan metodin esittelyyn lisätä annotaatio "@Payload Message foobar", jolla
        // päästäisiin käsiksi vastaanotettuun dataan (Perinteisessä http-pyynnössä samaiseen osaan päästään käsiksi
        // annotaatiolla @RequestBody).
        // Annotaatio @DestinationVariable täydentää aaltosuluissa olevav "foo" {channel} ulospäin lähetettävään osoitteeseen
        // (ilman WebSockettia aanotaationa olisi ollut http-protokollalle @PathVariable)

        // MIKA: Tallennetaan editorin sisältö Tallenna-painiketta napsauttaessa
        Document asiakirja = channelData.get(channel);
        asiakirja.setChannelName(channel); // Lisätään tallennettavaan olioon "kanava-tabin" nimi
        int id = tallennaTietokantaan.save(asiakirja).getId();  // Mahdollistaa tietokantaan generoidun yksillöllisen avaimen arvon
                                                                // palauttamisen asiakaselain-clientille.
//        return ResponseEntity.ok("Tallennettu tietokantaan ID:llä " + id + ". Palaa takaisin <a href='http://codelive-client.herokuapp.com/'>etusivulle</a>)");
    }

}
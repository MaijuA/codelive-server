package fi.academy.codeliveserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jari Haavisto
 *
 * ChannelData-luokan olio pitää sisällään datan aktiivisista kanavista.
 * Auki olevan kanavan sisältö on talletettu map-muuttujaan documents,
 * jonka avaimena on kanavan nimi ja arvona dokumentin sisältö. Auki
 * olevan kanavan käyttäjät ovat map-muuttujassa users, jonka avaimena
 * on kanavan nimi ja arvona lista käyttäjistä.
 *
 * Kun kanava tyhjenee, se suljetaan, jolloin molemmista map-tyyppisistä
 * muuttujista tuhotaan kanavan nimeä vastaavat arvot.
 */
@Component("channelData")
public class ChannelData implements Serializable{
    private Map<String, Document> documents;
    private Map<String, List<String>> users;

    public ChannelData() {
        this.documents = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void subscribe(String channel, String user) {
        if (!users.containsKey(channel)) {
            users.put(channel, new ArrayList<>());
        }
        users.get(channel).add(user);
    }

    @Autowired  // MIKA: Yhdistää DocumentRepository nimisen rajapinnan käsittelemään dataa
                // (kommunikoitaessa tietokannan kanssa) tämän java-luokan kautta
    private DocumentRepository tallennaTietokantaan; // MIKA: Annetaan repolle alias

    public void unsubscribe(String channel, String user) {
        if (users.containsKey(channel) && users.get(channel).contains(user)) {
            if (users.get(channel).size() == 1) {

                // MIKA: Kanavan Viimeisen käyttäjän sammuttaessa selaimen tai sulkiessa rastista "kanava-tabin"
                // tallennetaan taustalla automaattisesti editorin sisältö talteen tietokantaan
                Document asiakirja = documents.get(channel);
                asiakirja.setChannelName(channel); // Lisätään tallennettavaan olioon "kanava-tabin" nimi
                tallennaTietokantaan.save(documents.get(channel)).getId();

                users.remove(channel);
            } else {
                users.get(channel).remove(user);
            }
        }
    }


    public boolean contains(String key) {
        return documents.containsKey(key);
    }

    public void put(String key, Document value) {
        documents.put(key, value);
    }

    public Document get(String channel) {
        if (!documents.containsKey(channel)) {
            documents.put(channel, new Document());
        }
        return documents.get(channel);
    }

    public List<String> getUsers(String channel) {
        return users.get(channel);
    }
}

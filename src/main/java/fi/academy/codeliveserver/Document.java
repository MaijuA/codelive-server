package fi.academy.codeliveserver;

/**
 * Created by Jari Haavisto
 */


// MIKA: Muokattu application.properties tiedostoa eli paikallinen SQL-kanta "editorlive" (create database editorlive;)
import javax.persistence.*;

// MIKA: Alla oleva käyttää toistaiseksi JPA (Java Persistence API), kunnes saadaan muunnettua Hibenate natiiviksi
@Entity
/*
Tämä ilmoittaa Hibernatelle, että tee tästä Java-luokasta tietokantataulu. Tässä Java-luokassa on oltava julkinen tai
protected-näkyvyydelle määritelty parametriton konstruktori (oletuskonstruktori riittää),
tämä luokka ei voi olla final tai enum (etukäteen määritellyt tyypit omaava), tälle luokalle täytyy luoda getterit ja setterit
*/

// MIKA: Tässä voidaan antaa tietokantataulun nimi mikäli luokan nimi on eroava eli halutaan käyttää aliasta
@Table(name = "ASIAKIRJA") // Hibernate natiivilla ilman nimeämistä oletusarvoinen taulu luotaisiin nimellä EVENT
public class Document {

    @Id // MIKA: Tämä määrittää entiteetin identiteetin yhdeksi kentäksi
//    @GeneratedValue(generator = "increment") // Hibernate natiivilla + alla oleva annotaatio
//    @GenericGenerator(name = "increment", strategy = "increment") // Hibernate natiivilla
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @Lob // MIKA: String on oletusarvoisesti VARCHAR(255), lisäämällä annotaation @Lob tulee tyypiksi TEXT (voi tosin riippua kannasta!)
    private StringBuilder text;

    private String filename;

    public Document() {
        this.text = new StringBuilder();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String text) {
        this.text = new StringBuilder(text);
    }

    public void delete(int start, int end) {
        text.delete(start, end);
    }

    public void insert(int offset, String insertText) {
        text.insert(offset, insertText);
    }

    public void insert(int start, int end, String insertText) {
        delete(start, end);
        insert(start, insertText);

    }
}



package fi.academy.codeliveserver;

/**
 * Created by Jari Haavisto
 */


// MIKA: Muokattu application.properties tiedostoa eli paikallinen SQL-kanta "editorlive" (create database editorlive;)
// MIKA: Käytetään toistaiseksi JPA (Java Persistence API), kunnes saadaan muunnettua Hibenate natiiviksi
//import javax.persistence.*;

//@Entity
/*
MIKA: @Entity ilmoittaa Hibernatelle, että tee tästä Java-luokasta tietokantataulu. Tässä Java-luokassa on oltava julkinen tai
protected-näkyvyydelle määritelty parametriton konstruktori (oletuskonstruktori riittää),
tämä luokka ei voi olla final tai enum (etukäteen määritellyt tyypit omaava), tälle luokalle täytyy luoda getterit ja setterit
*/

//@Access(AccessType.PROPERTY)
/*
MIKA: @Access(AccessType.PROPERTY) muuttaa Java-luokan ja tietokantataulun mäppäyksen
tehtäväksi gettereiden perusteella eikä jäsenmuuttujien. Tämän vuoksi annotaatiota @Id ja sen mukana olevaa
tietoa yksilöllisestä avaimesta @GeneratedValue(strategy= GenerationType.AUTO)
ei sidota jäsenmuuttujaan vaan getteriin
*/


// MIKA: Tässä voidaan antaa tietokantataulun nimi mikäli luokan nimi on eroava eli halutaan käyttää aliasta
//@Table(name = "ASIAKIRJA") // Hibernate natiivilla ilman nimeämistä oletusarvoinen taulu luotaisiin nimellä EVENT
public class Document {

    // MIKA: Tästä on poistettu annotattio @Id, koska @Access(AccessType.PROPERTY)
    private int id;

    //@Transient// MIKA: Pyrkimys tulkita tietokantaan tallennettava teksti muodossa String eikä StringBuilder (toimii 11.12.2017 ilman tätäkin).
    private StringBuilder stringBuilder;
    private String text;
    private String filename;


    public Document() {
        this.stringBuilder = new StringBuilder();
    }

    //@Lob // MIKA: String on oletusarvoisesti VARCHAR(255), lisäämällä annotaation @Lob tulee tyypiksi TEXT (voi tosin riippua kannasta!)
    public String getText() {
        return stringBuilder.toString();
    }

    public void setText(String text) {
        this.stringBuilder = new StringBuilder(text);
    }

    //@Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public void delete(int start, int end) {
        stringBuilder.delete(start, end);
    }

    public void insert(int offset, String insertText) {
        stringBuilder.insert(offset, insertText);
    }

    public void insert(int start, int end, String insertText) {
        delete(start, end);
        insert(start, insertText);

    }
}



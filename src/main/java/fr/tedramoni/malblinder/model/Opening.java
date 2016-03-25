package fr.tedramoni.malblinder.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ted on 22/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("opening")
public class Opening {
    private Integer id;
    private String title;
    private String artist;

    public Opening(Integer id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Opening{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                '}';
    }

    public void setOpening(String line){
        Pattern patternTitre = Pattern.compile("\"(.*?)\"");
        Matcher matcherTitre = patternTitre.matcher(line);
        if (matcherTitre.find()) {
            this.setTitle(matcherTitre.group(1));
        }

        Pattern patternArtiste = Pattern.compile("by(.*?)\\(");
        Matcher matcherArtiste = patternArtiste.matcher(line);
        if (matcherArtiste.find()) {
            this.setArtist(matcherArtiste.group(1).trim());
        }
        else{
            String[] cuttedByEspace = line.split(" by ");
            this.setArtist(cuttedByEspace[cuttedByEspace.length-1]);
        }
    }
}

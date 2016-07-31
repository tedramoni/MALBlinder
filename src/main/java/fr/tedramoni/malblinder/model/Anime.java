package fr.tedramoni.malblinder.model;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.internal.NotNull;
import org.junit.Ignore;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ted on 22/03/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement(name = "anime")
public class Anime {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("titre")
    private String titre;

    @JsonProperty("openings")
    private Map<Integer,Opening> openings;

    private String img;

    private String status;

    private Random random;

    public Anime() {
    }

    public Integer getId() {
        return id;
    }

    @XmlElement(name="series_animedb_id")
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    @XmlElement(name="series_title")
    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getImg() {
        return img;
    }

    @XmlElement(name="series_image")
    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    @XmlElement(name="my_status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Map<Integer, Opening> getOpenings() {
        return openings;
    }

    public Opening getOpening(Integer index){
        return openings.get(index);
    }

    @JsonIgnore
    public Opening getRandomOpening(){
            random = new SecureRandom();
            int index = random.nextInt(openings.size() - 1 + 1) + 1;
            return openings.get(index);
    }

    @JsonIgnore
    public boolean hasOpening(){
        boolean hasOpening;
        if (openings.size() == 0) hasOpening = false;
        else hasOpening = true;
        return hasOpening;
    }

    public void addOpening(Integer id, Opening opening){
        this.openings.put(id, opening);
    }

    @Override
    public String toString() {
        return "Anime{" +
                "openings=" + openings +
                ", id=" + id +
                ", titre='" + titre + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

}

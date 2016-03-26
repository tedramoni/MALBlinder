package fr.tedramoni.malblinder.model;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.internal.NotNull;
import org.junit.Ignore;

import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ted on 22/03/2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Anime {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("titre")
    private String titre;
    @JsonProperty("openings")
    private Map<Integer,Opening> openings;
    private Random random;

    public Anime() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Map<Integer, Opening> getOpenings() {
        return openings;
    }

    public Opening getOpening(Integer index){
        return openings.get(index);
    }

    @JsonIgnore
    public Opening getRandomOpening(){
        random = new Random();
        int index = random.nextInt(openings.size() - 1 + 1) + 1;
        return openings.get(index);
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
                '}';
    }
}

package fr.tedramoni.malblinder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Ted on 22/03/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("anime")
public class Anime {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer id;
    private String titre;
    private Map<Integer,Opening> openings;
    @JsonIgnore
    private Random random;

    public Anime(Integer id) {
        this.id = id ;
        openings = new HashMap<Integer, Opening>();
        random = new Random();
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
        int index = random.nextInt(openings.size());
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

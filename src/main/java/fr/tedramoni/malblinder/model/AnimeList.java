package fr.tedramoni.malblinder.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Ted on 22/03/2016.
 */
@XmlRootElement(name = "myanimelist")
public class AnimeList {

    private Integer id;

    private User user;

    private List<Anime> animes;

    private Random random;

    public AnimeList(int id) {
        this.id = id;
        animes = new ArrayList<Anime>();
        random = new SecureRandom();
    }

    public AnimeList() {
    }

    public User getUser() {
        return user;
    }

    @XmlElement(name = "myinfo")
    public void setUser(User user) {
        this.user = user;
    }

    public List<Anime> getAnimes() {
        return animes;
    }

    @XmlElement(name = "anime")
    public void setAnimes(List<Anime> animes) {
        this.animes = animes;
    }

    public void addAnime(Anime anime) {
        this.animes.add(anime);
    }

    public Anime getAnime(int index) {
        return this.animes.get(index);
    }

    public void setAnime(int index, Anime anime) {
        animes.set(index, anime);
    }

    public Anime getRandomAnime() {
        int index = random.nextInt(animes.size());
        return this.animes.get(index);
    }

    @Override
    public String toString() {
        return "AnimeList{" +
                "id=" + id +
                ", user=" + user +
                ", animes=" + animes +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void removePTW() {
        List<Anime> list = new ArrayList<Anime>();
        for (Anime anime: this.animes) {
            if(anime != null){
                if (anime.getStatus() != null) {
                    if (anime.getStatus().equals("6") || anime.getStatus().equals("4")) {
                        list.add(anime);
                    }
                }
            }
        }
        this.animes.removeAll(list);
    }
}


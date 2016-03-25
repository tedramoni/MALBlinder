package fr.tedramoni.malblinder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ted on 22/03/2016.
 */
public class AnimeList {
    private Integer id;
    User user;
    List<Anime> animes;
    private Random random;

    public AnimeList(int id) {
        this.id = id;
        animes = new ArrayList<Anime>();
        random = new Random();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Anime> getAnimes() {
        return animes;
    }

    public void addAnime(Anime anime){
        this.animes.add(anime);
    }

    public Anime getAnime(int index){
        return this.animes.get(index);
    }

    public Anime getRandomAnime(){
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
}

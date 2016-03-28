
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
import fr.tedramoni.malblinder.client.youtube.ISearch;
import fr.tedramoni.malblinder.config.RestAPIConfig;
import fr.tedramoni.malblinder.config.RestYoutubeConfig;
import fr.tedramoni.malblinder.model.Anime;
import fr.tedramoni.malblinder.model.AnimeList;
import fr.tedramoni.malblinder.model.Opening;
import fr.tedramoni.malblinder.model.Video;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestAPIConfig.class, RestYoutubeConfig.class})
public class ClientTest {

    @Autowired
    private IClientRest client;

    @Autowired
    private ISearch search;

    private static final Logger log = Logger.getLogger(ClientTest.class);

    @Test
    public void ping() {
        Boolean pong = false;
        pong = client.ping();
        assertEquals(true, pong);
    }

    @Test
    public void testGetAnime() {
        Response reponse = client.getAnime("21");
        ObjectMapper mapper = new ObjectMapper();
        Anime anime = null;
        try {
            anime = mapper.readValue(reponse.readEntity(String.class), Anime.class);
            log.debug(anime);
        } catch (JsonParseException e1) {
            e1.printStackTrace();
        } catch (JsonMappingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        assertEquals("One Piece", anime.getTitre());
    }

    @Test
    public void testGetAnimeVideo() {
        Response reponse = client.getAnime("5081");
        ObjectMapper mapper = new ObjectMapper();
        Anime anime = null;
        try {
            anime = mapper.readValue(reponse.readEntity(String.class), Anime.class);
            Opening opening = anime.getRandomOpening();
            String keywords = opening.getTitle() + " " + opening.getArtist();
            log.debug(keywords);
            Video video = search.go(keywords);
            log.debug(video);
        } catch (JsonParseException e1) {
            e1.printStackTrace();
        } catch (JsonMappingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        assertEquals("Bakemonogatari", anime.getTitre());
    }

    @Test
    public void testGetAnimeList() {
        Response reponse = client.getAnimeList("Kmeuh");
        AnimeList animeList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(reponse.readEntity(String.class));
            animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
            log.debug(animeList);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        log.debug("Animes dans la liste :" + animeList.getAnimes().size());
        assertEquals("Kmeuh", animeList.getUser().getUsername());
    }

    @Test
    public void testGetAnimeListOpenings() {
        Response reponse = client.getAnimeList("Speleo99");
        AnimeList animeList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(reponse.readEntity(String.class));
            animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
            animeList.setId(1);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Integer index = 1;
        for (Anime anime : animeList.getAnimes()) {
            //try {
            ObjectMapper mapper = new ObjectMapper();
            Response reponse2 = client.getAnime(anime.getId().toString());
            String animeLine = reponse2.readEntity(String.class);
            log.debug(animeLine);
            //animeList.setAnime(index, mapper.readValue(reponse2.readEntity(String.class), Anime.class));
            ++index;
            /*} catch (JsonParseException e1) {
                e1.printStackTrace();
            } catch (JsonMappingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }*/
        }
        log.debug(animeList.getAnimes().size());
        assertEquals("Speleo99", animeList.getUser().getUsername());
    }
}
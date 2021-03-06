
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
import fr.tedramoni.malblinder.client.youtube.ISearch;
import fr.tedramoni.malblinder.config.RestAPIConfig;
import fr.tedramoni.malblinder.config.RestYoutubeConfig;
import fr.tedramoni.malblinder.model.*;
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
    public void pingMAL() {
        Boolean pong = false;
        pong = client.pingMAL();
        assertEquals(true, pong);
    }

    @Test
    public void pingYT() {
        Boolean pong = false;
        pong = client.pingYT();
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
        //Response reponse = client.getAnime("32245");
        Response reponse = client.getAnime("12875");
        ObjectMapper mapper = new ObjectMapper();
        Anime anime = null;
        try {
            anime = mapper.readValue(reponse.readEntity(String.class), Anime.class);
            Opening opening = anime.getOpening(1);
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
        assertEquals("Kuromukuro", anime.getTitre());
    }

    @Test
    public void testGetAnimeList() {
        Response reponse = client.getAnimeList("Elstorm92");
        AnimeList animeList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(reponse.readEntity(String.class));
            animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
            animeList.removePTW();
            log.debug(animeList);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        log.debug("Animes dans la liste :" + animeList.getAnimes().size());
        assertEquals("Elstorm92", animeList.getUser().getUsername());
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

    @Test
    public void searchAnime() {
        Response reponse = client.searchAnime("bleach");
        SearchList searchList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SearchList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(reponse.readEntity(String.class));
            searchList = (SearchList) jaxbUnmarshaller.unmarshal(reader);
            log.debug(searchList);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        assertEquals((Integer) 269, searchList.getEntry().get(0).getId());
    }
}
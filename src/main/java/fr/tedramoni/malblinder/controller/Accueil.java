package fr.tedramoni.malblinder.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
import fr.tedramoni.malblinder.client.youtube.ISearch;
import fr.tedramoni.malblinder.model.Anime;
import fr.tedramoni.malblinder.model.AnimeList;
import fr.tedramoni.malblinder.model.Opening;
import fr.tedramoni.malblinder.model.Video;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Scope("session")
public class Accueil {

    @Autowired
    private IClientRest clientRest;

    @Autowired
    private ISearch search;

    private static final Logger log = Logger.getLogger(Accueil.class.getName());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView accueil(HttpServletRequest request, Model model) {
        Boolean ping = clientRest.ping();
        model.addAttribute("ping",ping);
        return new ModelAndView("index", "model", model);
    }

    @RequestMapping(value = "/do", method = RequestMethod.POST)
    public ModelAndView doAccueil(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Response reponse = clientRest.getAnime(id);
        ObjectMapper mapper = new ObjectMapper();
        Anime anime = null;
        try {
            anime = mapper.readValue(reponse.readEntity(String.class), Anime.class);
            model.addAttribute("anime",anime);

            Map<Integer,Video> videos = new HashMap<>();
            Map<Integer,Opening> listOps = anime.getOpenings();
            for(int i=1;i<=listOps.size();i++){
                String keywords = listOps.get(i).getTitle()+' '+listOps.get(i).getArtist();
                Video video = search.go(keywords);
                log.debug(video);
                videos.put(i,video);
            }
            model.addAttribute("videos", videos);
            log.debug(videos);
            if(anime.getOpenings().size()>0){
                Opening opening = anime.getRandomOpening();
                model.addAttribute("opening",opening);
                String keywords = opening.getTitle()+' '+opening.getArtist();
                Video video = search.go(keywords);
                model.addAttribute("video", video);
                log.debug(video);
            }

        } catch (JsonParseException e1) {
            return new ModelAndView("redirect:/erreur");
        } catch (JsonMappingException e1) {
            return new ModelAndView("redirect:/erreur");
        } catch (IOException e1) {
            return new ModelAndView("redirect:/erreur");
        }
        return new ModelAndView("anime", "model", model);
    }

    @RequestMapping(value = "/getAnimeList", method = RequestMethod.POST)
    public ModelAndView getAnimeList(HttpServletRequest request, Model model) {
        String pseudo = request.getParameter("pseudo");
        Response reponse = clientRest.getAnimeList(pseudo);
        AnimeList animeList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(reponse.readEntity(String.class));
            animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
            animeList.setId(1);
            if(animeList.getUser().getUsername().isEmpty() || animeList.getUser().getUsername() == null){
                return new ModelAndView("redirect:/erreur");
            }
            model.addAttribute("animeList", animeList);
        } catch (JAXBException e) {
            return new ModelAndView("redirect:/erreur");
        }
        return new ModelAndView("animeList", "model", model);
    }

    @RequestMapping(value = "/erreur")
    public ModelAndView erreur(HttpServletRequest request, Model model) {
        return new ModelAndView("/layout/erreur", "model", model);
    }
}

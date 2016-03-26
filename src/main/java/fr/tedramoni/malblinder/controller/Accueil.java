package fr.tedramoni.malblinder.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
import fr.tedramoni.malblinder.client.youtube.ISearch;
import fr.tedramoni.malblinder.model.Anime;
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
import java.io.IOException;

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
            Opening opening = anime.getRandomOpening();
            model.addAttribute("opening",opening);
            String keywords = opening.getTitle()+' '+opening.getArtist();
            Video video = search.go(keywords);
            model.addAttribute("video", video);
            log.debug(video);
        } catch (JsonParseException e1) {
            e1.printStackTrace();
        } catch (JsonMappingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return new ModelAndView("anime", "model", model);
    }
}

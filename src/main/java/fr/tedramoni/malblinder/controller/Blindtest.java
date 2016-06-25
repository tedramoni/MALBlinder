package fr.tedramoni.malblinder.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
import fr.tedramoni.malblinder.client.youtube.ISearch;
import fr.tedramoni.malblinder.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created by Ted on 24/06/2016.
 */
@Controller
@Scope("session")
public class Blindtest {
    @Autowired
    private IClientRest clientRest;

    @Autowired
    private ISearch search;

    @RequestMapping(value = "/blindtest", method = RequestMethod.GET)
    public ModelAndView blindtest(HttpServletRequest request, Model model) {
        UserList userList = new UserList();
        model.addAttribute("userList", userList);
        return new ModelAndView("/blindtest/comptes", "model", model);
    }

    @RequestMapping(value = "/blindtest/process", method = RequestMethod.POST)
    public ModelAndView blindtest_process(HttpServletRequest request, Model model, UserList userList) {
        AnimeList animeList_final = new AnimeList(0);
        for (User user : userList.getUsers()) {
            Response reponse = clientRest.getAnimeList(user.getUsername());
            AnimeList animeList = null;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(reponse.readEntity(String.class));
                animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
                animeList.setId(1);
                for (Anime anime : animeList.getAnimes()) {
                    if (!animeList_final.getAnimes().contains(anime))
                        animeList_final.getAnimes().add(anime);
                }
                if (animeList.getUser() == null || animeList.getUser().getUsername().isEmpty() || animeList.getUser().getUsername() == null) {
                    String erreur = "L'utilisateur n'existe pas !";
                    model.addAttribute("erreur", erreur);
                    return new ModelAndView("/blindtest/comptes", "model", model);
                }
                request.getSession().setAttribute("animeList_final", animeList_final);
                Response reponse2 = clientRest.getAnime(animeList_final.getRandomAnime().getId().toString());
                ObjectMapper mapper = new ObjectMapper();
                Anime randomAnime = mapper.readValue(reponse2.readEntity(String.class), Anime.class);
                model.addAttribute("anime", randomAnime);
                Opening opening = randomAnime.getRandomOpening();
                model.addAttribute("opening", opening);
                String keywords = opening.getTitle() + " " + opening.getArtist();
                Video video = search.go(keywords);
                model.addAttribute("video", video);

            } catch (JAXBException e) {
                String erreur = "Erreur quelqueparts...";
                model.addAttribute("erreur", erreur);
                return new ModelAndView("/blindtest/comptes", "model", model);
            } catch (JsonParseException e) {
                String erreur = "Erreur quelqueparts...";
                model.addAttribute("erreur", erreur);
                return new ModelAndView("/blindtest/comptes", "model", model);
            } catch (JsonMappingException e) {
                String erreur = "Erreur quelqueparts...";
                model.addAttribute("erreur", erreur);
                return new ModelAndView("/blindtest/comptes", "model", model);
            } catch (IOException e) {
                String erreur = "Erreur quelqueparts...";
                model.addAttribute("erreur", erreur);
                return new ModelAndView("/blindtest/comptes", "model", model);
            }
        }

        return new ModelAndView("anime_pif", "model", model);
    }


}

package fr.tedramoni.malblinder.controller;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        request.getSession().setAttribute("userList", userList);
        List<AnimeList> usersAnimeList = new ArrayList<AnimeList>();
        Integer i = 1;
        for (User user : userList.getUsers()) {
            Response reponse = clientRest.getAnimeList(user.getUsername());
            AnimeList animeList = null;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(AnimeList.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                StringReader reader = new StringReader(reponse.readEntity(String.class));
                animeList = (AnimeList) jaxbUnmarshaller.unmarshal(reader);
                animeList.setId(i);
                i = ++i;
                animeList.removePTW();
                usersAnimeList.add(animeList);
                request.getSession().setAttribute("usersAnimeList", usersAnimeList);
                for (Anime anime : animeList.getAnimes()) {
                    if (!animeList_final.getAnimes().contains(anime))
                        animeList_final.getAnimes().add(anime);
                }
                request.getSession().setAttribute("animeList_final", animeList_final);
                if (animeList.getUser() == null || animeList.getUser().getUsername().isEmpty() || animeList.getUser().getUsername() == null) {
                    String erreur = "Un des utilisateurs n'existe pas !";
                    model.addAttribute("erreur", erreur);
                    return new ModelAndView("/blindtest/comptes", "model", model);
                }
            } catch (JAXBException e) {
                String erreur =  e.toString();
                model.addAttribute("erreur", erreur);
                return new ModelAndView("/blindtest/comptes", "model", model);
            }
        }
        try {
            model = randomizer(request, model);
        }
        catch (IOException e) {
            String erreur =  e.toString();
            model.addAttribute("erreur", erreur);
            return new ModelAndView("/blindtest/comptes", "model", model);
        }

        return new ModelAndView("anime_pif", "model", model);
    }

    @RequestMapping(value = "/blindtest/process/again")
    public ModelAndView blindtest_process_again(HttpServletRequest request, Model model, UserList userList) {
        try {
            model = randomizer(request, model);
        } catch (IOException e) {
            String erreur =  e.toString();
            model.addAttribute("erreur", erreur);
            return new ModelAndView("anime_pif", "model", model);
        }
        return new ModelAndView("anime_pif", "model", model);
    }

    public Model randomizer(HttpServletRequest request, Model model) throws IOException {
        AnimeList animeList_final = (AnimeList) request.getSession().getAttribute("animeList_final");
        Anime basicRandomAnime = animeList_final.getRandomAnime();
        Response reponse2 = clientRest.getAnime(basicRandomAnime.getId().toString());
        ObjectMapper mapper = new ObjectMapper();
        Anime randomAnime = mapper.readValue(reponse2.readEntity(String.class), Anime.class);
        while (randomAnime.hasOpening() == false){
            basicRandomAnime = animeList_final.getRandomAnime();
            randomAnime = mapper.readValue(clientRest.getAnime(basicRandomAnime.getId().toString()).readEntity(String.class), Anime.class);
        }
        model = sacs(request, model, basicRandomAnime);
        model.addAttribute("anime", randomAnime);
        Opening opening = randomAnime.getRandomOpening();
        model.addAttribute("opening", opening);
        String keywords = opening.getTitle() + " " + opening.getArtist() + " ";
        Video video = search.go(keywords);
        model.addAttribute("video", video);
        return model;

    }

    public Model sacs(HttpServletRequest request, Model model, Anime randomAnime) throws IOException {
        List<String> sacs = new ArrayList<String>();
        List<AnimeList> usersAnimeList = (List<AnimeList>) request.getSession().getAttribute("usersAnimeList");
        for(AnimeList user : usersAnimeList){
            for(Anime anime : user.getAnimes()){
                if(anime.getTitre().equalsIgnoreCase(randomAnime.getTitre())){
                    sacs.add(user.getUser().getUsername());
                }
            }
        }
        model.addAttribute("sacs", sacs);
        return model;
    }

}

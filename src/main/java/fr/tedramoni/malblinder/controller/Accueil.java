package fr.tedramoni.malblinder.controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tedramoni.malblinder.client.IClientRest;
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

    private static final Logger log = Logger.getLogger(Accueil.class.getName());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView accueil(HttpServletRequest request, Model model) {
        Boolean ping = clientRest.ping();
        model.addAttribute("ping",ping);
        return new ModelAndView("index", "model", model);
    }
}

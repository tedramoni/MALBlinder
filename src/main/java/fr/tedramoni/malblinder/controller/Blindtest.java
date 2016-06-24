package fr.tedramoni.malblinder.controller;

import fr.tedramoni.malblinder.client.IClientRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Created by Ted on 24/06/2016.
 */
@Controller
@Scope("session")
public class Blindtest {
    @Autowired
    private IClientRest clientRest;

    @RequestMapping(value = "/blindtest")
    public ModelAndView blindtest(HttpServletRequest request, Model model) {
        return new ModelAndView("/blindtest/comptes", "model", model);
    }
}

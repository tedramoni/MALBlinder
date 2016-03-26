package fr.tedramoni.malblinder.client;

import javax.ws.rs.core.Response;

/**
 * Created by tramoni on 06/01/2016.
 */

public interface IClientRest {

    public String afficherReponse(Response response);

    public Boolean ping();

    public Response getAnime(String id);

    public Response getAnimeList(String pseudo);

    }

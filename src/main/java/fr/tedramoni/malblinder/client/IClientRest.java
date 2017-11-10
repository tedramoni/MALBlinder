package fr.tedramoni.malblinder.client;

import javax.ws.rs.core.Response;

/**
 * Created by tramoni on 06/01/2016.
 */

public interface IClientRest {

    public String afficherReponse(Response response);

    public Boolean ping();

    public Boolean pingMAL();

    public Boolean pingYT();

    public Response getAnime(String id);

    public Response getAnimeList(String pseudo);

    public Response searchAnime(String keyword);


}

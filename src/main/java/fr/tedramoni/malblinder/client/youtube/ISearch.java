package fr.tedramoni.malblinder.client.youtube;

import fr.tedramoni.malblinder.model.Video;
import org.springframework.stereotype.Component;

/**
 * Created by Ted on 26/03/2016.
 */

public interface ISearch {
    Video go(String keywords);

    void setApiKey(String apiKey);
}

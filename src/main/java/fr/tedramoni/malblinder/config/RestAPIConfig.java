package fr.tedramoni.malblinder.config;

import fr.tedramoni.malblinder.client.ClientRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
@PropertySource({"classpath:application.properties"})
public class RestAPIConfig {

    @Value( "${client.uri}" )
    private String uri;
    @Value( "${client.login}" )
    private String login;
    @Value( "${client.mdp}" )
    private String mdp;
    @Value( "${client.recette}" )
    private boolean recette;
    @Value( "${client.referer}" )
    private String referer;
    @Value( "${mal.uri}" )
    private String malUri;
    @Value( "${mal.root}" )
    private String malRoot;
    @Value( "${yt.url}" )
    private String ytUri;


    @Bean
    protected ClientRest instanceClient() throws NoSuchAlgorithmException, KeyManagementException {
        ClientRest client = new ClientRest();
        client.setUri(uri);
        client.setLogin(login);
        client.setMdp(mdp);
        client.setRecette(recette);
        client.setReferer(referer);
        client.setMalUri(malUri);
        client.setMalRoot(malRoot);
        client.setYtUri(ytUri);
        client.initClient();
        return client;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
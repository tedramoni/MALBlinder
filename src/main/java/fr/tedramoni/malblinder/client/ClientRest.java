package fr.tedramoni.malblinder.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.authentication.*;
import org.glassfish.jersey.filter.LoggingFilter;

import javax.net.ssl.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ClientRest implements IClientRest{

    private final static Logger log = Logger.getLogger(ClientRest.class.getName());

    private String uri;
    private String login;
    private String mdp;
    private boolean recette;
    private String referer;
    private String malUri;

    private Client client;

    public void initClient() throws NoSuchAlgorithmException, KeyManagementException {
        // CLIENT REST SSL
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        // TLSv1 = SSLv3.1
        SSLContext ctx = SSLContext.getInstance("TLSv1");
        System.setProperty("https.protocols", "TLSv1");
        ctx.init(null, certs, new SecureRandom());

        Client client = ClientBuilder.newBuilder()
                .hostnameVerifier(new TrustAllHostNameVerifier())
                .sslContext(ctx)
                .register(JacksonJsonProvider.class)
                .build()
                ;

        // SI ON EST SUR LE SERVEUR DE TEST IL NOUS FAUT L'AUTHENTIFICATION BASIC
        if(recette == true){
            // LOG CONSOLE
            log.setLevel(Level.ALL);
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new SimpleFormatter());
            log.addHandler(handler);
            client.register(new LoggingFilter(log, true));
            // AJOUT DE L'AUTHENT
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(login, mdp);
            client.register(feature);
        }
        this.client = client;
    }

    // LOGIQUE 'TRUST ALL' POUR LES CERTIFICATS
    TrustManager[] certs = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }
            }
    };

    public static class TrustAllHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }

    // GET & SET
    public Client getClient(){
        return client;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public boolean isRecette() {
        return recette;
    }

    public void setRecette(boolean recette) {
        this.recette = recette;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setMalUri(String malUri) {
        this.malUri = malUri;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // METHODES DU CLIENT REST

    public String afficherReponse(Response response){
        return response.readEntity(String.class);
    }

    public Boolean ping() {
        Boolean pong = false;
        WebTarget target =client.target(uri);
        Response response = target.
                path("ping").
                request().
                header("Referer", this.referer).
                header("Content-Type", "application/json;charset=UTF-8").
                get(Response.class);
        if(response.getStatus()==200) pong = true;
        return pong;
    }

    public Response getAnime(String id) {
        WebTarget target =client.target(uri);
        Response response = target.
                path("anime/"+id).
                request().
                header("Referer", this.referer).
                header("Content-Type", "application/json;charset=UTF-8").
                get(Response.class);
        return response;
    }

    public Response getAnimeList(String pseudo) {
        WebTarget target =client.target(malUri+"?u="+pseudo+"&status=completed&status=onhold&type=anime");
        Response response = target.
                request().
                get(Response.class);
        return response;
    }

}

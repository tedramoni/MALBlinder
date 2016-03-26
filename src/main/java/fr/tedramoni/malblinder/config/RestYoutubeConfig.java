package fr.tedramoni.malblinder.config;

import fr.tedramoni.malblinder.client.youtube.Search;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


@Configuration
@PropertySource({"classpath:youtube.properties"})
public class RestYoutubeConfig {

    @Value( "${youtube.apikey}" )
    private String apiKey;

    @Bean
    protected Search instanceSearch(){
        Search search = new Search();
        search.setApiKey(apiKey);
        return search;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
package br.com.ronan.springboot.client;

import java.util.Arrays;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.ronan.springboot.domain.Anime;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        Anime[] objects = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(objects));

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        ResponseEntity<List<Anime>> exchanges = new RestTemplate()
                .exchange(
                    "http://localhost:8080/animes/all", 
                    HttpMethod.GET, 
                    null, 
                    new ParameterizedTypeReference<>() {}
                );
        log.info(exchanges.getBody());

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        ResponseEntity<Anime> exchange = new RestTemplate()
                .exchange(
                    "http://localhost:8080/animes/{id}",
                    HttpMethod.GET, 
                    null, 
                    new ParameterizedTypeReference<>() {},
                    2
                );
        log.info(exchange.getBody());

        // System.out.println("");
        // System.out.println("----------------------------");
        // System.out.println("");

        // Anime anime = Anime.builder().name("Digimon Adventure 02").build();

        // Anime animePost = new RestTemplate()
        //         .postForObject(
        //             "http://localhost:8080/animes/",
        //                 anime,
        //             Anime.class
        //         );
        // log.info("Save anime {}", animePost);

        // System.out.println("");
        // System.out.println("----------------------------");
        // System.out.println("");

        Anime anime = Anime.builder().name("Samurai Champloo").build();

        ResponseEntity<Anime> animePost = new RestTemplate()
                .exchange(
                        "http://localhost:8080/animes/", 
                        HttpMethod.POST,
                        new HttpEntity<>(anime), 
                        Anime.class
                );
        log.info("Save anime {}", animePost);

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        Anime animeToBeUpdate = animePost.getBody();
        animeToBeUpdate.setName("Samurai Champloo 2");

        ResponseEntity<Void> exchangePut = new RestTemplate()
                .exchange(
                    "http://localhost:8080/animes/",
                    HttpMethod.PUT, 
                    new HttpEntity<>(animeToBeUpdate) {},
                    void.class);
        log.info(exchangePut);

        System.out.println("");
        System.out.println("----------------------------");
        System.out.println("");

        ResponseEntity<Void> exchangeDelete = new RestTemplate()
                .exchange(
                    "http://localhost:8080/animes/{id}", 
                    HttpMethod.DELETE, 
                    null, 
                    Void.class, 
                    33
                );
        log.info(exchangeDelete);
    }
}

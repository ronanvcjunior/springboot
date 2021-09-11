package br.com.ronan.springboot.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import br.com.ronan.springboot.domain.Anime;
import br.com.ronan.springboot.domain.SpringbootUser;
import br.com.ronan.springboot.repository.AnimeRepository;
import br.com.ronan.springboot.repository.SpringbootUserRepository;
import br.com.ronan.springboot.requests.AnimePostRequestBody;
import br.com.ronan.springboot.util.AnimeCreator;
import br.com.ronan.springboot.util.AnimePostRequestBodyCreator;
import br.com.ronan.springboot.util.SpringbootUserCreator;
import br.com.ronan.springboot.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AnimeControllerIT {

    @Autowired
    @Qualifier(value = "testRestTemplateRoleUser")
    private TestRestTemplate testRestTemplateUser;

    @Autowired
    @Qualifier(value = "testRestTemplateRoleAdmin")
    private TestRestTemplate testRestTemplateAdmin;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private SpringbootUserRepository springbootUserRepository;

    @TestConfiguration
    @Lazy
    static class Config {

        @Bean(name = "testRestTemplateRoleUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port)
                    .basicAuthentication("user", "senha");

            return new TestRestTemplate(restTemplateBuilder);
        }

        @Bean(name = "testRestTemplateRoleAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port) {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri("http://localhost:" + port)
                    .basicAuthentication("admin", "senha");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("list returns list of anime inside page object when successful")
    void list_ReturnsListOfAnimesInsidePageObject_WhenSuccessful() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplateUser
                .exchange("/animes", HttpMethod.GET, null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of anime when successful")
    void listAll_ReturnsListOfAnimes_WhenSuccessful() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplateUser
                .exchange("/animes/all", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return anime when successuful")
    void findById_ReturnAnime_WhenSuccessuful() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();
        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplateUser.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getName()).isNotNull().isNotEqualTo("").isEqualTo(expectedName);

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName return list of anime when successuful")
    void findByName_ReturnListofAnime_WhenSuccessuful() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/find?name=%s", expectedName);

        List<Anime> animes = testRestTemplateUser
                .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return empty list of anime when anime is not found")
    void findByName_ReturnEmptyListofAnime_WhenAnimeIsNotFound() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        List<Anime> animes = testRestTemplateUser
                .exchange("/animes/find?name=dbz", HttpMethod.GET, null, new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Insert persists anime when successuful")
    void insert_PersistsAnime_WhenSuccessuful() {

        SpringbootUser admin = SpringbootUserCreator.creatSpringbootUserAdmin();

        springbootUserRepository.save(admin);

        AnimePostRequestBody animePostRequestBodyCreator = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplateAdmin.postForEntity("/animes",
                animePostRequestBodyCreator, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCodeValue()).isNotNull()
                .isEqualTo(HttpStatus.CREATED.value());

        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();

        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Delete remove anime when successuful")
    void delete_RemoveAnime_WhenSuccessuful() {

        SpringbootUser admin = SpringbootUserCreator.creatSpringbootUserAdmin();

        springbootUserRepository.save(admin);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplateAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE, null,
                Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCodeValue()).isNotNull()
                .isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @DisplayName("Delete returns 403 anime when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() {

        SpringbootUser user = SpringbootUserCreator.creatSpringbootUserUser();

        springbootUserRepository.save(user);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplateUser.exchange("/animes/admin/{id}",
                HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCodeValue()).isNotNull()
                .isEqualTo(HttpStatus.FORBIDDEN.value());

    }

    @Test
    @DisplayName("Replace updates anime when successuful")
    void replace_UpdatesAnime_WhenSuccessuful() {

        SpringbootUser admin = SpringbootUserCreator.creatSpringbootUserAdmin();

        springbootUserRepository.save(admin);

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        savedAnime.setName("new name");

        ResponseEntity<Void> animeResponseEntity = testRestTemplateAdmin.exchange("/animes", HttpMethod.PUT,
                new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();

        Assertions.assertThat(animeResponseEntity.getStatusCodeValue()).isNotNull()
                .isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
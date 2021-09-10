package br.com.ronan.springboot.controller;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ronan.springboot.domain.Anime;
import br.com.ronan.springboot.requests.AnimePostRequestBody;
import br.com.ronan.springboot.requests.AnimePutRequestBody;
import br.com.ronan.springboot.service.AnimeService;
import br.com.ronan.springboot.util.AnimeCreator;
import br.com.ronan.springboot.util.AnimePostRequestBodyCreator;
import br.com.ronan.springboot.util.AnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("tests for Anime Controller")
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;

    @Mock
    private AnimeService animeServiceMock;

    @BeforeEach
    void setUp() {
        List<Anime> animes = List.of(AnimeCreator.createAnimeValidAnime());
        PageImpl<Anime> animePage = new PageImpl<>(animes);

        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())).thenReturn(animePage);

        BDDMockito.when(animeServiceMock.listAllNonPageable()).thenReturn(animes);

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString())).thenReturn(animes);

        BDDMockito.when(animeServiceMock.insert(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeServiceMock).replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List returns list of anime inside page object when successuful")
    void list_ReturnsListOfAnimeInsidePageObject_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeController.list(null).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("ListAll returns list of anime when successuful")
    void listAll_ReturnsListOfAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animes = animeController.listAll().getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return anime when successuful")
    void findById_ReturnAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeController.findById(1).getBody();

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getName()).isNotNull().isNotEqualTo("").isEqualTo(expectedName);

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName return list of anime when successuful")
    void findByName_ReturnListofAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animes = animeController.findByName("").getBody();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return empty list of anime when anime is not found")
    void findByName_ReturnEmptyListofAnime_WhenAnimeIsNotFound() {
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn((Collections.emptyList()));

        List<Anime> animes = animeController.findByName("").getBody();

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Insert persists anime when successuful")
    void insert_PersistsAnime_WhenSuccessuful() {
        Anime anime = animeController.insert(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createAnimeValidAnime());
    }

    @Test
    @DisplayName("Delete remove anime when successuful")
    void delete_RemoveAnime_WhenSuccessuful() {
        ResponseEntity<Void> entity = animeController.delete(1);

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }

    @Test
    @DisplayName("Replace updates anime when successuful")
    void replace_UpdatesAnime_WhenSuccessuful() {
        ResponseEntity<Void> entity = animeController
                .replace(AnimePutRequestBodyCreator.createAnimePutRequestBodyCreator());

        Assertions.assertThat(entity).isNotNull();

        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    }
}

package br.com.ronan.springboot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.ronan.springboot.domain.Anime;
import br.com.ronan.springboot.exception.BadRequestException;
import br.com.ronan.springboot.repository.AnimeRepository;
import br.com.ronan.springboot.util.AnimeCreator;
import br.com.ronan.springboot.util.AnimePostRequestBodyCreator;
import br.com.ronan.springboot.util.AnimePutRequestBodyCreator;

@ExtendWith(SpringExtension.class)
@DisplayName("tests for Anime Controller")
class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp() {
        List<Anime> animes = List.of(AnimeCreator.createAnimeValidAnime());
        PageImpl<Anime> animePage = new PageImpl<>(animes);

        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class))).thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll()).thenReturn(animes);

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createAnimeValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString())).thenReturn(animes);

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createAnimeValidAnime());

        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("ListAll returns list of anime inside page object when successuful")
    void listAll_ReturnsListOfAnimeInsidePageObject_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);

        Assertions.assertThat(animePage.toList()).isNotEmpty().hasSize(1);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of anime when successuful")
    void listAllNonPageable_ReturnsListOfAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException return anime when successuful")
    void findByIdOrThrowBadRequestException_ReturnAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();
        Long expectedId = AnimeCreator.createAnimeValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1L);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getName()).isNotNull().isNotEqualTo("").isEqualTo(expectedName);

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("FindByName return list of anime when successuful")
    void findByName_ReturnListofAnime_WhenSuccessuful() {
        String expectedName = AnimeCreator.createAnimeValidAnime().getName();

        List<Anime> animes = animeService.findByName("");

        Assertions.assertThat(animes).isNotNull().isNotEmpty().hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return empty list of anime when anime is not found")
    void findByName_ReturnEmptyListofAnime_WhenAnimeIsNotFound() {
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn((Collections.emptyList()));

        List<Anime> animes = animeService.findByName("");

        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Insert persists anime when successuful")
    void insert_PersistsAnime_WhenSuccessuful() {
        Anime anime = animeService.insert(AnimePostRequestBodyCreator.createAnimePostRequestBody());

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createAnimeValidAnime());
    }

    @Test
    @DisplayName("Delete remove anime when successuful")
    void delete_RemoveAnime_WhenSuccessuful() {
        Assertions.assertThatCode(() -> animeService.delete(1)).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Replace updates anime when successuful")
    void replace_UpdatesAnime_WhenSuccessuful() {
        Assertions
                .assertThatCode(
                        () -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBodyCreator()))
                .doesNotThrowAnyException();

    }
}

package br.com.ronan.springboot.util;

import br.com.ronan.springboot.domain.Anime;

public class AnimeCreator {

    public static Anime createAnimeToBeSaved() {
        return Anime.builder().name("Digimon Adventure Tri").build();
    }

    public static Anime createAnimeValidAnime() {
        return Anime.builder().name("Digimon Adventure Tri").id(1L).build();
    }

    public static Anime createAnimeValidUpdateAnime() {
        return Anime.builder().name("Digimon Savers").id(1L).build();
    }
}

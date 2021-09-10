package br.com.ronan.springboot.util;

import br.com.ronan.springboot.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutRequestBody createAnimePutRequestBodyCreator() {
        return AnimePutRequestBody.builder().name(AnimeCreator.createAnimeValidUpdateAnime().getName())
                .id(AnimeCreator.createAnimeValidUpdateAnime().getId()).build();
    }
}

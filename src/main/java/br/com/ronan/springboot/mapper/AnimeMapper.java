package br.com.ronan.springboot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import br.com.ronan.springboot.domain.Anime;
import br.com.ronan.springboot.requests.AnimePostRequestBody;
import br.com.ronan.springboot.requests.AnimePutRequestBody;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    
    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
}

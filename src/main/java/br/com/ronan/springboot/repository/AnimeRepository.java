package br.com.ronan.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ronan.springboot.domain.Anime;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    Page<Anime> findAll(Pageable pageable);

    List<Anime> findByName(String name);
}


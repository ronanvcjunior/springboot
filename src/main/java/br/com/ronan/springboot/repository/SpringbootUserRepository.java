package br.com.ronan.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ronan.springboot.domain.SpringbootUser;

public interface SpringbootUserRepository extends JpaRepository<SpringbootUser, Long>{
    
    SpringbootUser findByUserName(String userName);
}

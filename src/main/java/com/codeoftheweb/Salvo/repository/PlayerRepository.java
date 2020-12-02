package com.codeoftheweb.Salvo.repository;

import java.util.List;

import com.codeoftheweb.Salvo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player, Long> {
//    List<Player> findByEmail(String email);
    Player findByEmail(@Param("name") String name);

}
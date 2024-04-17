package com.fct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fct.entities.*;
import java.util.Optional;


public interface PlayerRepository extends JpaRepository<Player,Integer>{
    Optional<Player> findByUsername(String username);
}
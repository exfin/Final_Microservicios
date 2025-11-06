package com.victims.victimsservice.dao;

import com.victims.victimsservice.model.Victim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VictimRepo extends JpaRepository<Victim, Long> {

    Optional<Victim> findByCode(String code);

    List<Victim> findByCreatedBy(Long userId);
}


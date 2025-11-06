package com.incentive.incentiveservice.repo;

import com.incentive.incentiveservice.enums.IncentiveType;
import com.incentive.incentiveservice.models.IncentiveEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IncentiveEventRepo extends JpaRepository<IncentiveEvent, Long> {

    List<IncentiveEvent> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<IncentiveEvent> findByTypeOrderByCreatedAtDesc(IncentiveType type);

    @Query("select coalesce(sum(i.points), 0) from IncentiveEvent i where i.user = :user")
    int sumPointsByUser(@Param("user") Long user);
}

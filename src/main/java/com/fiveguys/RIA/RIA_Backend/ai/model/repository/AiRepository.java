package com.fiveguys.RIA.RIA_Backend.ai.model.repository;

import com.fiveguys.RIA.RIA_Backend.ai.model.entity.Ai;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AiRepository extends JpaRepository<Ai, Long> {
    @Query(value = """
        SELECT *
        FROM ai
        WHERE VIP_ID = :vipId
        ORDER BY CREATED_AT DESC
        LIMIT 3
        """, nativeQuery = true)
    List<Ai> findTop3(@Param("vipId") Long vipId);
}

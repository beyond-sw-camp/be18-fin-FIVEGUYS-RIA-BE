package com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.pipeline.model.entity.Pipeline;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
  @Query("SELECT p FROM Pipeline p JOIN FETCH p.project WHERE p.id = :pipelineId")
  Optional<Pipeline> findByIdWithProject(@Param("pipelineId") Long pipelineId);
}
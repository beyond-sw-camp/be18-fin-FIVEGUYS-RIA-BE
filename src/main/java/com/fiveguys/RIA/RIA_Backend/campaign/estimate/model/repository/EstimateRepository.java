package com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.repository;

import com.fiveguys.RIA.RIA_Backend.campaign.estimate.model.entity.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
}

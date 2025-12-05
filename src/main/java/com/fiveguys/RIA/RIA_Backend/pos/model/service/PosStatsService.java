package com.fiveguys.RIA.RIA_Backend.pos.model.service;

import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;

import java.util.List;

public interface PosStatsService {

    List<PosRepository.BrandProductStats> getBrandProduct(Long customerId);
}

package com.fiveguys.RIA.RIA_Backend.pos.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import com.fiveguys.RIA.RIA_Backend.pos.model.service.PosStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosStatsServiceImpl implements PosStatsService {

    private final PosRepository posRepository;

    @Override
    public List<PosRepository.BrandProductStats> getBrandProduct(Long customerId) {
        return posRepository.findBrandProductStats(customerId);
    }
}

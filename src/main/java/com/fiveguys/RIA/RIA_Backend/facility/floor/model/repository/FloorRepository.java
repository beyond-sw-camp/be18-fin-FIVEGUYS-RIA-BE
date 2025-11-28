package com.fiveguys.RIA.RIA_Backend.facility.floor.model.repository;

import com.fiveguys.RIA.RIA_Backend.facility.floor.model.entity.Floor;
import com.fiveguys.RIA.RIA_Backend.facility.zone.model.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {


    @Query("SELECT f FROM Floor f WHERE f.zone = :zone ORDER BY f.floorName ASC")
    List<Floor> findFloorsByZone(Zone zone);
}

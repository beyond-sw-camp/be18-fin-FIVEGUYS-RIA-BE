package com.fiveguys.RIA.RIA_Backend.pos.model.repository;

import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PosRepository extends JpaRepository<Pos, Long> {

    interface BrandStats {
        String getBrandName();
        BigDecimal getTotalAmount();
        Long getPurchaseCount();
    }

    @Query("""
        select p.brandName as brandName,
               sum(p.amount) as totalAmount,
               count(p.id) as purchaseCount
        from Pos p
        where p.customerId = :customerId
        group by p.brandName
        order by purchaseCount desc
        """)
    List<BrandStats> findBrand(@Param("customerId") Long customerId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Pos p WHERE p.customerId = :customerId")
    Long getTotalSalesByCustomerId(@Param("customerId") Long customerId);
}


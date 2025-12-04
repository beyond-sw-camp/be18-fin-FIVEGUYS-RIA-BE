package com.fiveguys.RIA.RIA_Backend.pos.model.repository;

import com.fiveguys.RIA.RIA_Backend.pos.model.entity.Pos;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PosRepository extends JpaRepository<Pos, Long> {

    interface BrandProductStats {
        String getBrandName();
        String getProductName();
        BigDecimal getTotalAmount();
        Long getPurchaseCount();
    }

    @Query("""
        select p.brandName as brandName,
               p.productName as productName,
               sum(p.amount) as totalAmount,
               count(p.posId) as purchaseCount
        from Pos p
        where p.customerId = :customerId
          and p.productName is not null
        group by p.brandName, p.productName
        order by totalAmount desc
        """)
    List<BrandProductStats> findBrandProductStats(@Param("customerId") Long customerId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Pos p WHERE p.customerId = :customerId")
    Long getTotalSalesByCustomerId(@Param("customerId") Long customerId);
    List<Pos> findByPurchaseAtBetween(LocalDateTime from, LocalDateTime to);


}


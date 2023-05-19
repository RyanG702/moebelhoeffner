package com.ryangrillo.warehousemgt.api.repository;

import com.ryangrillo.warehousemgt.api.enums.BayType;
import com.ryangrillo.warehousemgt.api.model.Bay;
import com.ryangrillo.warehousemgt.api.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long > {
    @Transactional
    @Modifying
    @Query("DELETE FROM Warehouse w WHERE w.identifierCode = :identifierCode")
    void deleteByIdentifierCode(String identifierCode);

    Optional<Warehouse> findByName(String name);

    Optional<Warehouse> findByIdentifierCode(String identifierCode);

    @Modifying
    @Query("UPDATE Warehouse w SET w.lastBayAdded = :lastBayAdded WHERE w.id = :warehouseId")
    void updateLastBayAdded(@Param("warehouseId") Long warehouseId, @Param("lastBayAdded") String lastBayAdded);

}

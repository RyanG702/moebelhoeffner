package com.ryangrillo.warehousemgt.api.repository;

import com.ryangrillo.warehousemgt.api.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long > {


}

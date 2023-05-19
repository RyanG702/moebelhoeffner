package com.ryangrillo.warehousemgt.api.model;




import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long warehouseId;
    @Column(unique=true)
    private String name;
    @Column(unique=true, nullable = false)
    @Valid
    private String identifierCode;

    @OneToOne(mappedBy = "warehouse", cascade = CascadeType.ALL, optional = false)
    @PrimaryKeyJoinColumn
    @NotNull(message="address is required")
    private Address address;

    @OneToMany
    @JoinColumn(name = "warehouse")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JsonIgnore
    private List<Bay> bays;

    @JsonIgnore
    private String lastBayAdded;

    @NotNull
    @Min(value = 1, message = "Max Rows Value must be at least 1")
    private int maxRows;
    @NotNull
    @Min(value = 1, message = "Max Height Value must be at least 1")
    private int maxLevels;
    @NotNull
    @Min(value = 1, message = "Max Row Length Value must be at least 1")
    private int maxShelves;

    public Warehouse() {
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public String getLastBayAdded() {
        return lastBayAdded;
    }

    public void setLastBayAdded(String lastBayAdded) {
        this.lastBayAdded = lastBayAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifierCode() {
        return identifierCode;
    }

    public void setIdentifierCode(String identifierCode) {
        this.identifierCode = identifierCode;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Bay> getBays() {
        return bays;
    }

    public void setBays(List<Bay> bays) {
        this.bays = bays;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getMaxLevels() {
        return maxLevels;
    }

    public void setMaxLevels(int maxLevels) {
        this.maxLevels = maxLevels;
    }

    public int getMaxShelves() {
        return maxShelves;
    }

    public void setMaxShelves(int maxShelves) {
        this.maxShelves = maxShelves;
    }

}

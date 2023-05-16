package com.ryangrillo.warehousemgt.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames={"id", "rowNumber", "shelfNumber", "levelNumber"})
})
public class Bay {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @NotNull
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Tag> tags;

    @Column( nullable = false)
    private String rowNumber;

    @Column( nullable = false)
    private String shelfNumber;

    @Column( nullable = false)
    private String levelNumber;

    @Column(nullable = false)
    private boolean isPalletBay;

    @Column(nullable = false)
    private boolean isCartBay;

    @NotNull
    @Range(min = 1, max = 9)
    private int holdingPoints;

    @JoinColumn(name = "warehouse")
    @ManyToOne(targetEntity = Warehouse.class, fetch = FetchType.LAZY)
    //@NotNull(message = "Warehouse not set")
    @JsonIgnore
    private Warehouse warehouse;

    @Column(name = "warehouse", insertable = false, updatable = false)
    private Long warehouseId;

    public Bay() {
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(String levelNumber) {
        this.levelNumber = levelNumber;
    }

    public boolean getIsPalletBay() {
        return isPalletBay;
    }

    public void setIsPalletBay(boolean isPalletBay) {
        this.isPalletBay = isPalletBay;
    }

    public boolean getIsCartBay() {
        return isCartBay;
    }

    public void setIsCartBay(boolean isCartBay) {
        this.isCartBay = isCartBay;
    }

    public int getHoldingPoints() {
        return holdingPoints;
    }

    public void setHoldingPoints(int holdingPoints) {
        this.holdingPoints = holdingPoints;
    }


}

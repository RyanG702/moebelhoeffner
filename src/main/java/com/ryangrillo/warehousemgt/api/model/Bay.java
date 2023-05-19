package com.ryangrillo.warehousemgt.api.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ryangrillo.warehousemgt.api.enums.BayType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames={"bayId", "rowNumber", "shelfNumber", "levelNumber"})
})
public class Bay {
    @Id
    @GeneratedValue
    private Long bayId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @Column(unique=true, nullable = false)
    private String bayLabel;

    @OneToMany
    @NotNull(message = "Tags not set")
    @JoinColumn(name = "bay")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Tag> tags;

    @Column( nullable = false)
    private String rowNumber;

    @Column( nullable = false)
    private String shelfNumber;

    @Column( nullable = false)
    private String levelNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BayType bayType;

    @NotNull
    @Range(min = 1, max = 9)
    private int holdingPoints;

    @JsonIgnore
    private int holdingPointsUsed;

    @JoinColumn(name = "warehouse")
    @ManyToOne(targetEntity = Warehouse.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Warehouse warehouse;

    private LocalDateTime timestamp;

    public Bay() {
    }

    public Bay(String rowNumber, String shelfNumber, String levelNumber, String bayLabel, List<Tag> tags, BayType bayType, int holdingPoints, Warehouse warehouse) {
        this.rowNumber = rowNumber;
        this.shelfNumber = shelfNumber;
        this.levelNumber = levelNumber;
        this.bayLabel = bayLabel;
        this.tags = tags;
        this.bayType = bayType;
        this.holdingPoints = holdingPoints;
        this.warehouse = warehouse;
        this.timestamp = LocalDateTime.now();
    }

    public int getHoldingPointsUsed() {
        return holdingPointsUsed;
    }

    public void setHoldingPointsUsed(int holdingPointsUsed) {
        this.holdingPointsUsed = holdingPointsUsed;
    }

    public BayType getBayType() {
        return bayType;
    }

    public String getBayLabel() {
        return bayLabel;
    }

    public void setBayLabel(String bayLabel) {
        this.bayLabel = bayLabel;
    }

    public void setBayType(BayType bayType) {
        this.bayType = bayType;
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

    public int getHoldingPoints() {
        return holdingPoints;
    }

    public void setHoldingPoints(int holdingPoints) {
        this.holdingPoints = holdingPoints;
    }


}

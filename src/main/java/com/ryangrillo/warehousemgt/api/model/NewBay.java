package com.ryangrillo.warehousemgt.api.model;

import com.ryangrillo.warehousemgt.api.enums.BayType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public class NewBay {
    private List<Tag> tags;
    private BayType bayType;
    private int holdingPoints;

    public NewBay() {
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public BayType getBayType() {
        return bayType;
    }

    public void setBayType(BayType bayType) {
        this.bayType = bayType;
    }

    public int getHoldingPoints() {
        return holdingPoints;
    }

    public void setHoldingPoints(int holdingPoints) {
        this.holdingPoints = holdingPoints;
    }
}

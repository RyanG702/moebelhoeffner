package com.ryangrillo.warehousemgt.api.model;

public class HoldingPointsStatus {
    private int holdingPointsTotal;
    private int holdingPointsBusy;
    private int holdingPointsFree;

    public HoldingPointsStatus() {
    }

    public HoldingPointsStatus(int holdingPointsTotal, int holdingPointsBusy) {
        this.holdingPointsTotal = holdingPointsTotal;
        this.holdingPointsBusy = holdingPointsBusy;
        this.holdingPointsFree = holdingPointsTotal - holdingPointsBusy;
    }

    public int getHoldingPointsTotal() {
        return holdingPointsTotal;
    }

    public void setHoldingPointsTotal(int holdingPointsTotal) {
        this.holdingPointsTotal = holdingPointsTotal;
    }

    public int getHoldingPointsBusy() {
        return holdingPointsBusy;
    }

    public void setHoldingPointsBusy(int holdingPointsBusy) {
        this.holdingPointsBusy = holdingPointsBusy;
    }

    public int getHoldingPointsFree() {
        return holdingPointsFree;
    }

    public void setHoldingPointsFree(int holdingPointsFree) {
        this.holdingPointsFree = holdingPointsFree;
    }
}

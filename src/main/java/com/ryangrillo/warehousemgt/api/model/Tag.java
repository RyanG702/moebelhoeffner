package com.ryangrillo.warehousemgt.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true, nullable = false)
    @Pattern(regexp="[a-z]+", message="tag must be lowercase")
    private String name;

    @JoinColumn(name = "bay")
    @ManyToOne(targetEntity = Bay.class, fetch = FetchType.LAZY)
    //@NotNull(message = "Tag not set")
    @JsonIgnore
    private Bay bay;

    public Tag() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bay getBay() {
        return bay;
    }

    public void setBay(Bay bay) {
        this.bay = bay;
    }
}

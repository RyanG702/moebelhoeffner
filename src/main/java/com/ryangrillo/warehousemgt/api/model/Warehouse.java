package com.ryangrillo.warehousemgt.api.model;




import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String name;

    @Column(unique=true, nullable = false)
    @Size(min = 3, message = "{identifier code must be 3 digits}")
    @Size(max = 3, message = "{identifier code must be 3 digits}")
    @Valid
    private String identifierCode;

    @OneToOne(mappedBy = "warehouse", cascade = CascadeType.ALL, optional = false)
    private Address address;

    @OneToMany
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Bay> bays;

    public Warehouse() {
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

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", identifierCode='" + identifierCode + '\'' +
                ", address=" + address +
                ", bays=" + bays +
                '}';
    }
}

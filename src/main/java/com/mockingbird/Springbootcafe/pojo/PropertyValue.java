package com.mockingbird.Springbootcafe.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "propertyValue")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
@Getter
@Setter
public class PropertyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "ptid")
    private Property property;

    private String value;



}

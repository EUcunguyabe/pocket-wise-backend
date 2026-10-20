package com.auca.pocketwise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "savings_circle")
public class SavingsCircle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int circleNumber;

    private String name;

    public SavingsCircle() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getCircleNumber() {
        return circleNumber;
    }

    public void setCircleNumber(int circleNumber) {
        this.circleNumber = circleNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

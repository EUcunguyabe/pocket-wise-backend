package com.auca.pocketwise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coach")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    @ManyToOne
    @JoinColumn(name = "savings_circle_id")
    private SavingsCircle savingsCircle;

    @ManyToMany
    @JoinTable(
            name = "coach_expertise_area",
            joinColumns = @JoinColumn(name = "coach_id"),
            inverseJoinColumns = @JoinColumn(name = "expertise_area_id")
    )
    private List<ExpertiseArea> expertiseAreas = new ArrayList<>();

    public Coach() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SavingsCircle getSavingsCircle() {
        return savingsCircle;
    }

    public void setSavingsCircle(SavingsCircle savingsCircle) {
        this.savingsCircle = savingsCircle;
    }

    public List<ExpertiseArea> getExpertiseAreas() {
        return expertiseAreas;
    }

    public void setExpertiseAreas(List<ExpertiseArea> expertiseAreas) {
        this.expertiseAreas = expertiseAreas;
    }
}

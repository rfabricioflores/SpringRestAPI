package se.fabricioflores.springrestapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Role {

    public Role() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    private String name;
}

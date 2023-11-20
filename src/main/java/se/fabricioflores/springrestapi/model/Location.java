package se.fabricioflores.springrestapi.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
public class Location implements Serializable {

    public Location() {}

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "locations")
    private Set<Category> categories = new LinkedHashSet<>();

    private Long userId;

    private String accessibility;

    @Column(nullable = false)
    private LocalDateTime editedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String description;

    private Point<G2D> coordinate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Point<G2D> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Point<G2D> coordinate) {
        this.coordinate = coordinate;
    }
}

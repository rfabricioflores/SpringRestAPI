package se.fabricioflores.springrestapi.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.geolatte.geom.G2D;
import org.geolatte.geom.Point;

import static se.fabricioflores.springrestapi.databind.Point2DJsonMapper.Point2DDeserializer;
import static se.fabricioflores.springrestapi.databind.Point2DJsonMapper.Point2DSerializer;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
public class Location implements Serializable {

    public Location() {
    }

    public Location(
            String name,
            Accessibility accessibility,
            String description,
            Point<G2D> coordinate,
            Long userId
    ) {
        this.name = name;
        this.accessibility = accessibility;
        this.description = description;
        this.coordinate = coordinate;
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, updatable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Accessibility accessibility = Accessibility.PUBLIC;

    private Long userId;

    private String description;

    @ManyToMany(mappedBy = "locations", fetch = FetchType.EAGER)
    private Set<Category> categories = new LinkedHashSet<>();

    @JsonSerialize(using = Point2DSerializer.class)
    @JsonDeserialize(using = Point2DDeserializer.class)
    private Point<G2D> coordinate;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime editedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Location location = (Location) o;
        return getId() != null && Objects.equals(getId(), location.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

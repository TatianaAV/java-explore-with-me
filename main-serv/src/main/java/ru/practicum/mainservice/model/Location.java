package ru.practicum.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations", schema = "public")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return lat.equals(location.lat) && lon.equals(location.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}

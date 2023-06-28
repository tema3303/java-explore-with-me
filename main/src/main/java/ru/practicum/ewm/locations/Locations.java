package ru.practicum.ewm.locations;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Locations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "latitude", nullable = false)
    private Float lat;
    @Column(name = "longitude", nullable = false)
    private Float lon;
}
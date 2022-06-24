package com.mileage.wuga.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
public class Place {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID placeId;

    private String placeName;

    @OneToMany(
        mappedBy = "place",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true
    )
    private List<Review> reviewId = new ArrayList<>();

    public Place() {
    }

    private Place(UUID placeId) {
        this.placeId = placeId;
    }

    public static Place createPlaceId(String id) {
        return new Place(UUID.fromString(id));
    }
}

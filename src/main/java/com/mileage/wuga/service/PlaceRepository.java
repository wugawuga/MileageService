package com.mileage.wuga.service;

import com.mileage.wuga.domain.Place;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {

    Place findByPlaceId(UUID uuid);
}

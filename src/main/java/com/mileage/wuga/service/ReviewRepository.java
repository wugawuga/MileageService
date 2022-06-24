package com.mileage.wuga.service;

import com.mileage.wuga.domain.Place;
import com.mileage.wuga.domain.Review;
import com.mileage.wuga.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    List<Review> findByPlace(Place place);

    Review findByUserAndPlace(User user, Place place);
}

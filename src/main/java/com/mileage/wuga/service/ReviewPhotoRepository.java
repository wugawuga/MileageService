package com.mileage.wuga.service;

import com.mileage.wuga.domain.ReviewPhoto;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, UUID> {

}

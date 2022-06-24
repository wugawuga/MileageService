package com.mileage.wuga.service;

import com.mileage.wuga.domain.ReviewDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    public String check(ReviewDTO reviewDTO) {

        String type = reviewDTO.getType();
        if (type.equals("REVIEW")) {
            return "REVIEW";
        }
        return null;
    }
}

package com.mileage.wuga.controller;

import com.mileage.wuga.domain.ReviewDTO;
import com.mileage.wuga.service.EventService;
import com.mileage.wuga.service.ReviewMileageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EventController {

    private ReviewMileageService reviewMileageService;
    private EventService eventService;

    @PostMapping("/events")
    public String checkEvent(ReviewDTO reviewDTO) {

        String check = eventService.check(reviewDTO);

//        if (check.equals("REVIEW")) {
//            reviewMileageService.reviewCheck(reviewDTO);
//        } else {
//            throw new RuntimeException("없는 이벤트에요.");
//        }

        return null;
    }
}

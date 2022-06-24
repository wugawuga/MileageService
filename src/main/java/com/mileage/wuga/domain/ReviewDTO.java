package com.mileage.wuga.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDTO {

    private String type;
    private String action;
    private String reviewId;
    private String content;
    private String[] attachedPhotoIds;
    private String userId;
    private String placeId;
}

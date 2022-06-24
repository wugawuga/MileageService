package com.mileage.wuga.service;


import static com.mileage.wuga.domain.MileageLog.createLogStatus;
import static com.mileage.wuga.domain.MileageStatus.ADD_CONTENT;
import static com.mileage.wuga.domain.MileageStatus.ADD_CONTENT_FIRST;
import static com.mileage.wuga.domain.MileageStatus.ADD_CONTENT_IMAGE;
import static com.mileage.wuga.domain.MileageStatus.ADD_CONTENT_IMAGE_FIRST;
import static com.mileage.wuga.domain.MileageStatus.ADD_IMAGE;
import static com.mileage.wuga.domain.MileageStatus.ADD_IMAGE_FIRST;
import static com.mileage.wuga.domain.MileageStatus.DEL_CONTENT;
import static com.mileage.wuga.domain.Review.createReview;

import com.mileage.wuga.domain.MileageLog;
import com.mileage.wuga.domain.MileageStatus;
import com.mileage.wuga.domain.Place;
import com.mileage.wuga.domain.Review;
import com.mileage.wuga.domain.ReviewDTO;
import com.mileage.wuga.domain.ReviewPhoto;
import com.mileage.wuga.domain.User;
import com.mileage.wuga.exception.ExistReviewAtPlaceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewMileageService {

    private final ReviewRepository reviewRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ReviewPhotoRepository reviewPhotoRepository;
    private final MileageLogRepository mileageLogRepository;

    public Review actionCheck(ReviewDTO reviewDTO) {

        String action = reviewDTO.getAction();

        if (action.equals("ADD")) {
            return reviewCheck(reviewDTO);
        } else if (action.equals("MOD")) {
            return modifyReview(reviewDTO);
        } else {
            return deleteReview(reviewDTO);
        }
    }

    private Review deleteReview(ReviewDTO reviewDTO) {
        return null;
    }

    private Review modifyReview(ReviewDTO reviewDTO) {

        User user = userRepository.findByUserId(UUID.fromString(reviewDTO.getUserId()));
        Place place = placeRepository.findByPlaceId(UUID.fromString(reviewDTO.getPlaceId()));

        Optional<Review> byUserAndPlace = Optional.ofNullable(
            reviewRepository.findByUserAndPlace(user, place)
        );

        if (byUserAndPlace.isEmpty()) {
            throw new ExistReviewAtPlaceException("리뷰가 없습니다");
        }
        Review review = byUserAndPlace.get();

        String content = review.getContent();
        String contentByDTO = reviewDTO.getContent();
        List<ReviewPhoto> reviewPhoto = review.getReviewPhoto();
        String[] attachedPhotoIds = reviewDTO.getAttachedPhotoIds();

        if ((content == null || content.length() == 0) && contentByDTO.length() >= 1) {
            if (reviewPhoto.size() == 0 && attachedPhotoIds.length >= 1) {
                for (String attachedPhotoId : reviewDTO.getAttachedPhotoIds()) {
                    ReviewPhoto reviewPhotos = new ReviewPhoto();
                    reviewPhotos.setFileName(attachedPhotoId);
                    review.add(reviewPhotos);
                    reviewPhotoRepository.save(reviewPhotos);
                }
                review.setContent(contentByDTO);
                user.createMileage(2);
                createMileageLog(ADD_CONTENT_IMAGE);
            } else {
                review.setContent(contentByDTO);
                user.createMileage(1);
                createMileageLog(ADD_CONTENT);
            }
        } else if (content.length() >= 1 && (contentByDTO == null || contentByDTO.length() == 0)) {
            review.setContent(contentByDTO);
            user.createMileage(-1);
            createMileageLog(DEL_CONTENT);
        } else {
            review.setContent(contentByDTO);
        }
        return null;
    }

    public Review reviewCheck(ReviewDTO reviewDTO) {

        User user = userRepository.findByUserId(UUID.fromString(reviewDTO.getUserId()));
        Place place = placeRepository.findByPlaceId(UUID.fromString(reviewDTO.getPlaceId()));

        Optional<Review> byUserAndPlace = Optional.ofNullable(
            reviewRepository.findByUserAndPlace(user, place)
        );

        List<Review> byPlace = reviewRepository.findByPlace(place);

        if (byUserAndPlace.isPresent()) {
            throw new ExistReviewAtPlaceException("존재합니다");
        }
        if (byPlace.size() == 0) {
            addFirstMileage(reviewDTO, user);
        } else {
            addMileage(reviewDTO, user);
        }
        return addReview(reviewDTO, user, place);
    }

    private void addMileage(ReviewDTO reviewDTO, User user) {
        if (reviewDTO.getAttachedPhotoIds() == null) {
            createMileageLog(ADD_CONTENT);
            user.createMileage(1);
        }
        if (reviewDTO.getContent() == null || reviewDTO.getContent().equals("")) {
            createMileageLog(ADD_IMAGE);
            user.createMileage(1);
        }
        createMileageLog(ADD_CONTENT_IMAGE);
        user.createMileage(2);
    }

    private void addFirstMileage(ReviewDTO reviewDTO, User user) {
        if (reviewDTO.getAttachedPhotoIds() == null) {
            createMileageLog(ADD_CONTENT_FIRST);
            user.createMileage(2);
        }
        if (reviewDTO.getContent() == null || reviewDTO.getContent().equals("")) {
            createMileageLog(ADD_IMAGE_FIRST);
            user.createMileage(2);
        }
        createMileageLog(ADD_CONTENT_IMAGE_FIRST);
        user.createMileage(3);
    }

    private Review addReview(ReviewDTO reviewDTO, User user, Place place) {

        Review review = reviewRepository.save(
            createReview(reviewDTO.getContent(), user, place));
        for (String attachedPhotoId : reviewDTO.getAttachedPhotoIds()) {
            ReviewPhoto reviewPhoto = new ReviewPhoto();
            reviewPhoto.setFileName(attachedPhotoId);
            review.add(reviewPhoto);
            reviewPhotoRepository.save(reviewPhoto);
        }
        if ((reviewDTO.getContent() == null || reviewDTO.getContent().equals(""))
            && reviewDTO.getAttachedPhotoIds() == null) {
            throw new RuntimeException("내용도 없고 사진도 없는데??");
        }

        return review;
    }

    private void createMileageLog(MileageStatus mileageStatus) {

        MileageLog logStatus = createLogStatus(mileageStatus);
        mileageLogRepository.save(logStatus);
    }
}

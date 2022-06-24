package com.mileage.wuga.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mileage.wuga.domain.Place;
import com.mileage.wuga.domain.Review;
import com.mileage.wuga.domain.ReviewDTO;
import com.mileage.wuga.domain.User;
import com.mileage.wuga.exception.ExistReviewAtPlaceException;
import com.mileage.wuga.service.PlaceRepository;
import com.mileage.wuga.service.ReviewMileageService;
import com.mileage.wuga.service.ReviewRepository;
import com.mileage.wuga.service.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ReviewControllerTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewMileageService reviewMileageService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void 유저등록() throws Exception {

        User user = new User();
        user.setUserName("yun");

        userRepository.save(user);

        User byUserId = userRepository.findByUserId(user.getUserId());
        assertThat(user.getUserName()).isEqualTo(byUserId.getUserName());
    }

    @Test
    @Transactional
    public void 장소등록() throws Exception {

        Place place = new Place();
        place.setPlaceName("커피커피커피집");
        placeRepository.save(place);

        Place byPlaceId = placeRepository.findByPlaceId(place.getPlaceId());

        assertThat(place.getPlaceName()).isEqualTo(byPlaceId.getPlaceName());
    }

    @Test
    @Transactional
    public void 리뷰등록() throws Exception {

        User user = new User();
        user.setUserName("yun");
        userRepository.save(user);

        Place place = new Place();
        place.setPlaceName("커피커피커피집");
        placeRepository.save(place);

        Review review = new Review();
        review.setUser(user);
        review.setPlace(place);

        reviewRepository.save(review);

        List<Place> all = placeRepository.findAll();
        Place placeForList = all.get(0);

        List<Review> byPlace = reviewRepository.findByPlace(placeForList);

        for (Review review1 : byPlace) {
            System.out.println("review user = " + review1.getUser().getUserName());
            System.out.println("review place = " + review1.getPlace().getPlaceName());
        }
    }

    @Test
    @Transactional
    public void 유저조회() throws Exception {

        User user = new User();
        user.setUserName("yun");
        userRepository.save(user);

        UUID userId = user.getUserId();
        Optional<User> byId = userRepository.findById(userId);

        assertThat(userId).isEqualTo(byId.get().getUserId());
    }

    @Test
    @Transactional
    public void 리뷰재등록() throws Exception {

        User user = new User();
        user.setUserName("yun");
        userRepository.save(user);

        Place place = new Place();
        place.setPlaceName("커피커피커피집");
        placeRepository.save(place);

        Review review = new Review();
        review.setUser(user);
        review.setPlace(place);
        review.setContent("하하");
        reviewRepository.save(review);


        ReviewDTO dto = new ReviewDTO();
        dto.setUserId(user.getUserId().toString());
        dto.setPlaceId(place.getPlaceId().toString());
        dto.setType("REVIEW");
        dto.setAction("ADD");

        assertThatThrownBy(() -> reviewMileageService.actionCheck(dto))
            .isInstanceOf(ExistReviewAtPlaceException.class)
            .hasMessage("존재합니다");
    }

    @Test
    @Transactional
    public void 작성한리뷰없을때_정상적으로_등록() throws Exception {

        User user = new User();
        user.setUserName("yun");
        userRepository.save(user);

        Place place = new Place();
        place.setPlaceName("커피커피커피집");
        placeRepository.save(place);

        ReviewDTO reviewDTO = new ReviewDTO();
        String[] filename = {"1", "2"};
        reviewDTO.setAction("ADD");
        reviewDTO.setType("REVIEW");
        reviewDTO.setContent("좋아요!");
        reviewDTO.setUserId(user.getUserId().toString());
        reviewDTO.setPlaceId(place.getPlaceId().toString());
        reviewDTO.setAttachedPhotoIds(filename);

        Review review = reviewMileageService.actionCheck(reviewDTO);

        assertThat(review.getUser().getUserId()).isEqualTo(user.getUserId());
        assertThat(review.getReviewPhoto().get(0).getReview().getReviewId()).isEqualTo(review.getReviewId());
    }

    @Test
    public void 같은장소_두번재_댓글작성시_마일리지() throws Exception {

        User user = new User();
        user.setUserName("yun");
        userRepository.save(user);
        User user1 = new User();
        user1.setUserName("wugawuga");
        userRepository.save(user1);

        Place place = new Place();
        place.setPlaceName("커피커피커피집");
        placeRepository.save(place);

        ReviewDTO reviewDTO = new ReviewDTO();
        String[] filename = {"1", "2"};
        reviewDTO.setAction("ADD");
        reviewDTO.setType("REVIEW");
        reviewDTO.setContent("좋아요!");
        reviewDTO.setUserId(user.getUserId().toString());
        reviewDTO.setPlaceId(place.getPlaceId().toString());
        reviewDTO.setAttachedPhotoIds(filename);

        ReviewDTO reviewDTO1 = new ReviewDTO();
        String[] filename1 = {"1", "2"};
        reviewDTO1.setAction("ADD");
        reviewDTO1.setType("REVIEW");
        reviewDTO1.setContent("좋아요!");
        reviewDTO1.setUserId(user1.getUserId().toString());
        reviewDTO1.setPlaceId(place.getPlaceId().toString());
        reviewDTO1.setAttachedPhotoIds(filename1);

        Review review = reviewMileageService.actionCheck(reviewDTO);
        Review review1 = reviewMileageService.actionCheck(reviewDTO1);

    }
}
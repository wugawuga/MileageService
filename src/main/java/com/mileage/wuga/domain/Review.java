package com.mileage.wuga.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "reviews", indexes = {
    @Index(columnList = "userId"),
    @Index(columnList = "placeId")
})
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID reviewId;

    private String content;

    @OneToMany(
        mappedBy = "review",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        orphanRemoval = true
    )
    private List<ReviewPhoto> reviewPhoto = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeId")
    private Place place;

    public Review() {
    }

    private Review(String content, User user, Place place) {
        this.content = content;
        this.user = user;
        this.place = place;
    }

    public static Review createReview(String content, User user, Place place) {

        return new Review(
            content,
            user,
            place
        );
    }

    public void add(ReviewPhoto reviewPhoto) {
        reviewPhoto.setReview(this);
        this.reviewPhoto.add(reviewPhoto);
    }
}

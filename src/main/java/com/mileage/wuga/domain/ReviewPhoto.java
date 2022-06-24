package com.mileage.wuga.domain;

import static javax.persistence.FetchType.LAZY;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
public class ReviewPhoto {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID attachedPhotoId;

    private String fileName;

    private String filePath;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "reviewId")
    private Review review;

}

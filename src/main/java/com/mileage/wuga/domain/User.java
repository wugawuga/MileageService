package com.mileage.wuga.domain;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Setter
@Getter
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID userId;

    private String userName;

    @Column(name = "userMileage", columnDefinition = "int(5) default 0")
    private Integer mileage;

    @OneToMany(mappedBy = "user")
    private List<Review> members = new ArrayList<>();

    public User() {
    }

    private User(UUID userId) {
        this.userId = userId;
    }

    public void createMileage(Integer mileage) {
        setMileage(this.mileage + mileage);
    }
}

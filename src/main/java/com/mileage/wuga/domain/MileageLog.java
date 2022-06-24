package com.mileage.wuga.domain;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
public class MileageLog {

    @Id
    @GeneratedValue
    private Long id;

    private String logTime;

    @Enumerated(EnumType.STRING)
    private MileageStatus status;

    @PrePersist
    public void onPrePersist(){
        this.logTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    @PreUpdate
    public void onPreUpdate(){
        this.logTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
    }

    private MileageLog(MileageStatus status) {
        this.status = status;
    }

    public static MileageLog createLogStatus(MileageStatus status) {
        return new MileageLog(status);
    }
}

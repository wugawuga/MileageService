package com.mileage.wuga.service;

import com.mileage.wuga.domain.MileageLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageLogRepository extends JpaRepository<MileageLog, Long> {

}

package com.survey.api.message.repo;

import com.survey.api.message.entity.SptsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SptsMessageRepository extends JpaRepository<SptsMessage, Long> {
    List<SptsMessage> findByGroupsIdAndStartDateLessThanEqualAndEndDateGreaterThan(Long groupsId, Date currentDate1,Date currentDate2);

}


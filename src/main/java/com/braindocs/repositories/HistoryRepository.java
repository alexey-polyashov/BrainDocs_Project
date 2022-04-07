package com.braindocs.repositories;

import com.braindocs.models.HistoryStampModel;
import com.braindocs.models.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryStampModel, Long> {

    List<HistoryStampModel> findAllByChangeType(String changeType);

    List<HistoryStampModel> findAllByCreateTimeBetween(LocalDateTime since, LocalDateTime before);
}

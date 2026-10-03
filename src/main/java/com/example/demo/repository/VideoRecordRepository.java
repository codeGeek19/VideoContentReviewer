package com.example.demo.repository;

import com.example.demo.model.Status;
import com.example.demo.model.VideoRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {
    List<VideoRecord> findByTitleContainingIgnoreCase(String title);
    List<VideoRecord> findByStatus(Status status);
    List<VideoRecord> findByTitleContainingIgnoreCaseAndStatus(String title, Status status);
    long countByStatus(Status status);
}
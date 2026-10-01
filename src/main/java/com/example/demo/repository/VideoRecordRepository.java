package com.example.demo.repository;

import com.example.demo.model.VideoRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {}
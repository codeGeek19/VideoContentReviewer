package com.example.demo.service;

import com.example.demo.model.Status;
import com.example.demo.model.VideoRecord;
import com.example.demo.repository.VideoRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class VideoRecordService {
    private final VideoRecordRepository repo;

    public VideoRecordService(VideoRecordRepository repo) { this.repo = repo; }

    public List<VideoRecord> findAll() { return repo.findAll(); }

    public VideoRecord findById(Long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Record " + id + " not found"));
    }

    public VideoRecord create(VideoRecord v) {
        v.setStatus(Status.SUBMITTED);
        return repo.save(v);
    }
}
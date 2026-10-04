package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.Status;
import com.example.demo.model.VideoRecord;
import com.example.demo.repository.VideoRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public List<VideoRecord> search(String q, Status status) {
        boolean hasQ = q != null && !q.isBlank();
        if (hasQ && status != null) return repo.findByTitleContainingIgnoreCaseAndStatus(q, status);
        if (hasQ) return repo.findByTitleContainingIgnoreCase(q);
        if (status != null) return repo.findByStatus(status);
        return repo.findAll();
    }

    public VideoRecord update(Long id, VideoRecord in) {
        VideoRecord v = findById(id);
        v.setTitle(in.getTitle());
        v.setVideoUrl(in.getVideoUrl());
        v.setDescription(in.getDescription());
        v.setReviewer(in.getReviewer());
        return repo.save(v);
    }

    public VideoRecord transition(Long id, Status target, Role role) {
        VideoRecord v = findById(id);
        Status cur = v.getStatus();
        boolean ok =
                (role == Role.REVIEWER && cur == Status.SUBMITTED && target == Status.IN_REVIEW) ||
                        (role == Role.REVIEWER && cur == Status.IN_REVIEW
                                && (target == Status.APPROVED || target == Status.REJECTED)) ||
                        (role == Role.SUBMITTER && cur == Status.REJECTED && target == Status.SUBMITTED);
        if (!ok) throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                role + " cannot move " + cur + " -> " + target);
        v.setStatus(target);
        return repo.save(v);
    }

    public Map<String, Long> dashboard() {
        Map<String, Long> m = new LinkedHashMap<>();
        for (Status s : Status.values()) m.put(s.name(), repo.countByStatus(s));
        m.put("TOTAL", repo.count());
        return m;
    }
}

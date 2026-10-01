package com.example.demo.controller;

import com.example.demo.model.VideoRecord;
import com.example.demo.service.VideoRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class VideoRecordController {
    private final VideoRecordService service;

    public VideoRecordController(VideoRecordService service) { this.service = service; }

    @GetMapping
    public List<VideoRecord> list() { return service.findAll(); }

    @GetMapping("/{id}")
    public VideoRecord get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideoRecord create(@Valid @RequestBody VideoRecord v) { return service.create(v); }
}
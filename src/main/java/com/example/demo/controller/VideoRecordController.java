package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.Status;
import com.example.demo.model.VideoRecord;
import com.example.demo.service.VideoRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
public class VideoRecordController {
    private final VideoRecordService service;

    public VideoRecordController(VideoRecordService service) { this.service = service; }

    @GetMapping
    public List<VideoRecord> list(@RequestParam(required = false) String q,
                                  @RequestParam(required = false) Status status) {
        return service.search(q, status);
    }

    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() { return service.dashboard(); }

    @GetMapping("/{id}")
    public VideoRecord get(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VideoRecord create(@Valid @RequestBody VideoRecord v) { return service.create(v); }

    @PutMapping("/{id}")
    public VideoRecord update(@PathVariable Long id, @Valid @RequestBody VideoRecord v) {
        return service.update(id, v);
    }

    @PatchMapping("/{id}/status")
    public VideoRecord changeStatus(@PathVariable Long id,
                                    @RequestParam Status target,
                                    @RequestHeader(value = "X-Role", defaultValue = "SUBMITTER") Role role) {
        return service.transition(id, target, role);
    }
}
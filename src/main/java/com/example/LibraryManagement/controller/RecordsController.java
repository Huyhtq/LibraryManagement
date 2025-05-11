package com.example.LibraryManagement.controller;

import com.example.LibraryManagement.dto.RecordsDTO;
import com.example.LibraryManagement.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class RecordsController {

    private final RecordsService recordsService;

    @Autowired
    public RecordsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }

    @GetMapping
    public List<RecordsDTO> getAllRecords() {
        return recordsService.getAllRecords();
    }

    @GetMapping("/{id}")
    public RecordsDTO getRecordById(@PathVariable Long id) {
        return recordsService.getRecordById(id);
    }

    @PostMapping
    public RecordsDTO createRecord(@RequestBody RecordsDTO dto) {
        return recordsService.createRecord(dto);
    }

    @PutMapping("/{id}")
    public RecordsDTO updateRecord(@PathVariable Long id, @RequestBody RecordsDTO dto) {
        return recordsService.updateRecord(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        recordsService.deleteRecord(id);
    }
}

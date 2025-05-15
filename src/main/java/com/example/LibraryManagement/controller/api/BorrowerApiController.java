package com.example.LibraryManagement.controller.api;

import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerApiController {

    private final BorrowerService borrowerService;

    @Autowired
    public BorrowerApiController(BorrowerService borrowerService) {
        this.borrowerService = borrowerService;
    }

    @GetMapping
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerService.getAllBorrowers();
    }

    @GetMapping("/{id}")
    public BorrowerDTO getBorrowerById(@PathVariable Long id) {
        return borrowerService.getBorrowerById(id);
    }

    @PostMapping
    public BorrowerDTO createBorrower(@RequestBody BorrowerDTO dto) {
        return borrowerService.createBorrower(dto);
    }

    @PutMapping("/{id}")
    public BorrowerDTO updateBorrower(@PathVariable Long id, @RequestBody BorrowerDTO dto) {
        return borrowerService.updateBorrower(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
    }
}

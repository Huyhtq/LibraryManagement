package com.example.LibraryManagement.service.impl;

import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.mapper.BorrowerMapper;
import com.example.LibraryManagement.entity.Borrower;
import com.example.LibraryManagement.repository.BorrowerRepository;
import com.example.LibraryManagement.service.BorrowerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository; // Không cần @Autowired

    @Override
    public List<BorrowerDTO> getAllBorrowers() {
        return borrowerRepository.findAll().stream()
                .map(BorrowerMapper::toDTO)
                .filter(Objects::nonNull) // Loại bỏ các DTO null nếu có
                .collect(Collectors.toList());
    }

    @Override
    public BorrowerDTO getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(BorrowerMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Borrower with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public BorrowerDTO createBorrower(BorrowerDTO dto) {
        Borrower borrower = BorrowerMapper.toEntity(dto);
        return BorrowerMapper.toDTO(borrowerRepository.save(borrower));
    }

    @Override
    @Transactional
    public BorrowerDTO updateBorrower(Long id, BorrowerDTO dto) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Borrower with ID " + id + " not found"));

        if (dto.getName() != null) borrower.setName(dto.getName());
        if (dto.getEmail() != null) borrower.setEmail(dto.getEmail());
        if (dto.getPhone() != null) borrower.setPhone(dto.getPhone());
        if (dto.getBorrowCount() != null) borrower.setBorrowCount(dto.getBorrowCount());

        return BorrowerMapper.toDTO(borrowerRepository.save(borrower));
    }

    @Override
    @Transactional
    public void deleteBorrower(Long id) {
        borrowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Borrower with ID " + id + " not found"));
        borrowerRepository.deleteById(id);
    }

    @Override
    public Page<BorrowerDTO> getAllBorrowers(Pageable pageable) {
        return borrowerRepository.findAll(pageable)
                .map(BorrowerMapper::toDTO);
    }

    // Thêm phương thức sử dụng các query tùy chỉnh trong BorrowerRepository
    public BorrowerDTO getBorrowerByEmail(String email) {
        return borrowerRepository.findByEmail(email)
                .map(BorrowerMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Borrower with email " + email + " not found"));
    }

    public List<BorrowerDTO> getBorrowersByMinBorrowCount(int minBorrowCount) {
        return borrowerRepository.findByBorrowCountGreaterThan(minBorrowCount).stream()
                .map(BorrowerMapper::toDTO)
                .collect(Collectors.toList());
    }
}
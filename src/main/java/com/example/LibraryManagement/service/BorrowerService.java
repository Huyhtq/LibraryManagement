package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.BorrowerDTO;
import java.util.*;

public interface BorrowerService {
    List<BorrowerDTO> getAllBorrowers();
    BorrowerDTO getBorrowerById(Long id);
    BorrowerDTO createBorrower(BorrowerDTO dto);
    BorrowerDTO updateBorrower(Long id, BorrowerDTO dto);
    void deleteBorrower(Long id);

}

package com.example.LibraryManagement.service;

import com.example.LibraryManagement.dto.RecordsDTO;
import java.util.*;

public interface RecordsService {
    List<RecordsDTO> getAllRecords();
    RecordsDTO getRecordById(Long id);
    RecordsDTO createRecord(RecordsDTO dto);
    RecordsDTO updateRecord(Long id, RecordsDTO dto);
    void deleteRecord(Long id);

}

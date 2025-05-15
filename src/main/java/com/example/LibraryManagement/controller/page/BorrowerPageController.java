package com.example.LibraryManagement.controller.page;

import com.example.LibraryManagement.dto.BorrowerDTO;
import com.example.LibraryManagement.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/borrowers")
@RequiredArgsConstructor
public class BorrowerPageController {

    @Autowired
    private BorrowerService borrowerService;

    @GetMapping
    public String listBorrowers(Model model) {
        model.addAttribute("borrowers", borrowerService.getAllBorrowers());
        return "borrowers";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("borrower", new BorrowerDTO());
        model.addAttribute("formTitle", "Add New Borrower");
        return "borrower-form";
    }

    @PostMapping
    public String createBorrower(@ModelAttribute BorrowerDTO borrower) {
        borrowerService.createBorrower(borrower);
        return "redirect:/borrowers";
    }

    @PostMapping("/save")
    public String saveBorrower(@ModelAttribute("borrower") BorrowerDTO borrowerDTO) {
        if (borrowerDTO.getId() == null) {
            borrowerService.createBorrower(borrowerDTO);
        } else {
            borrowerService.updateBorrower(borrowerDTO.getId(), borrowerDTO);
        }
        return "redirect:/borrowers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        BorrowerDTO dto = borrowerService.getBorrowerById(id);
        model.addAttribute("borrower", dto);
        model.addAttribute("formTitle", "Edit Borrower");
        return "borrower-form";
    }

    @PostMapping("/update/{id}")
    public String updateBorrower(@PathVariable Long id, @ModelAttribute BorrowerDTO borrower) {
        borrowerService.updateBorrower(id, borrower);
        return "redirect:/borrowers";
    }

    @GetMapping("/delete/{id}")
    public String deleteBorrower(@PathVariable Long id) {
        borrowerService.deleteBorrower(id);
        return "redirect:/borrowers";
    }

    @GetMapping("/search")
    public String searchBorrowerByEmail(@RequestParam String email, Model model) {
        BorrowerDTO borrower = borrowerService.getBorrowerByEmail(email);
        model.addAttribute("borrower", borrower);
        return "borrower-details";
    }
}

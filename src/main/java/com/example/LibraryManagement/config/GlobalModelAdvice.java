package com.example.LibraryManagement.config;

import com.example.LibraryManagement.dto.AccountDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addAccountToModel(HttpSession session, Model model) {
        AccountDTO accountDTO = (AccountDTO) session.getAttribute("account");
        if (accountDTO != null) {
            model.addAttribute("accountDTO", accountDTO);
        }
    }
}

package com.sams.sams.controller;

import com.sams.sams.model.DepositRecord;
import com.sams.sams.repository.DepositRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DepositController {

    @Autowired private DepositRecordRepository depositRecordRepository;

    @GetMapping("/deposits")
    public String deposits(Model model) {
        List<DepositRecord> all = depositRecordRepository.findAll();
        model.addAttribute("deposits", all);
        model.addAttribute("totalCount", all.size());
        model.addAttribute("activeCount", depositRecordRepository.countByStatus("Active"));
        model.addAttribute("movedOutCount", depositRecordRepository.countByStatus("MovedOut"));
        model.addAttribute("refundedCount", depositRecordRepository.countByStatus("Refunded"));
        model.addAttribute("activePage", "deposits-page");
        return "deposits";
    }

    @PostMapping("/deposits/add")
    public String addDeposit(@ModelAttribute DepositRecord deposit) {
        deposit.setStatus("Active");
        depositRecordRepository.save(deposit);
        return "redirect:/deposits";
    }

    @PostMapping("/deposits/moveout/{id}")
    public String moveOut(@PathVariable Long id,
                          @RequestParam String moveOutDate,
                          @RequestParam String moveOutCondition,
                          @RequestParam(defaultValue = "0") double deductionAmount,
                          @RequestParam(required = false) String deductionReason) {
        DepositRecord d = depositRecordRepository.findById(id).orElse(null);
        if (d != null) {
            d.setMoveOutDate(moveOutDate);
            d.setMoveOutCondition(moveOutCondition);
            d.setDeductionAmount(deductionAmount);
            d.setDeductionReason(deductionReason);
            d.setRefundAmount(d.getDepositAmount() - deductionAmount);
            d.setStatus("MovedOut");
            depositRecordRepository.save(d);
        }
        return "redirect:/deposits";
    }

    @PostMapping("/deposits/refund/{id}")
    public String markRefunded(@PathVariable Long id) {
        DepositRecord d = depositRecordRepository.findById(id).orElse(null);
        if (d != null) {
            d.setStatus("Refunded");
            depositRecordRepository.save(d);
        }
        return "redirect:/deposits";
    }

    @PostMapping("/deposits/delete/{id}")
    public String deleteDeposit(@PathVariable Long id) {
        depositRecordRepository.deleteById(id);
        return "redirect:/deposits";
    }
}
package com.sams.sams.service;
import com.sams.sams.model.Complaint;
import com.sams.sams.repository.ComplaintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ComplaintService {
    @Autowired private ComplaintRepository complaintRepository;
    public List<Complaint> getAllComplaints() { return complaintRepository.findAll(); }
    public Complaint getComplaintById(Long id) { return complaintRepository.findById(id).orElse(null); }
    public Complaint saveComplaint(Complaint c) {
        if (c.getId() == null) {
            c.setStatus("Open");
            c.setCreatedDate(LocalDate.now().toString());
        }
        return complaintRepository.save(c);
    }
    public Complaint updateStatus(Long id, String status) {
        Complaint c = getComplaintById(id);
        if (c == null) return null;
        c.setStatus(status);
        if ("Resolved".equalsIgnoreCase(status)) c.setResolvedDate(LocalDate.now().toString());
        return complaintRepository.save(c);
    }
    public void deleteComplaint(Long id) { complaintRepository.deleteById(id); }
}
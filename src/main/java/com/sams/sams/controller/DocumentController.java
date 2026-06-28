package com.sams.sams.controller;

import com.sams.sams.model.TenantDocument;
import com.sams.sams.model.Tenant;
import com.sams.sams.model.User;
import com.sams.sams.repository.DocumentRepository;
import com.sams.sams.repository.TenantRepository;
import com.sams.sams.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class DocumentController {

    @Autowired private DocumentRepository documentRepository;
    @Autowired private TenantRepository tenantRepository;
    @Autowired private UserRepository userRepository;

    private static final String UPLOAD_DIR = "uploads/documents/";

    // Admin view all documents
    @GetMapping("/documents")
    public String adminDocuments(Model model) {
        List<TenantDocument> docs = documentRepository.findAll();
        model.addAttribute("documents", docs);
        model.addAttribute("totalCount", docs.size());
        model.addAttribute("activePage", "documents");
        return "documents";
    }

    // Tenant view own documents
    @GetMapping("/my-documents")
    public String myDocuments(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/login";

        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null) {
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
        }

        List<TenantDocument> myDocs = tenant != null
                ? documentRepository.findByApartmentNumber(tenant.getApartmentNumber())
                : List.of();

        model.addAttribute("tenant", tenant);
        model.addAttribute("documents", myDocs);
        model.addAttribute("activePage", "documents");
        return "my-documents";
    }

    // Upload document
    @PostMapping("/documents/upload")
    public String uploadDocument(@RequestParam String documentName,
                                 @RequestParam String documentType,
                                 @RequestParam String tenantName,
                                 @RequestParam String apartmentNumber,
                                 @RequestParam MultipartFile file,
                                 HttpSession session) throws IOException {

        String username = (String) session.getAttribute("username");
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        file.transferTo(new File(UPLOAD_DIR + fileName));

        TenantDocument doc = new TenantDocument();
        doc.setDocumentName(documentName);
        doc.setDocumentType(documentType);
        doc.setTenantName(tenantName);
        doc.setApartmentNumber(apartmentNumber);
        doc.setFileName(fileName);
        doc.setUploadDate(LocalDate.now().toString());
        doc.setUploadedBy(username);
        documentRepository.save(doc);

        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role) ? "redirect:/documents" : "redirect:/my-documents";
    }

    // Tenant upload own document
    @PostMapping("/my-documents/upload")
    public String tenantUploadDocument(@RequestParam String documentName,
                                       @RequestParam String documentType,
                                       @RequestParam MultipartFile file,
                                       HttpSession session) throws IOException {

        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElse(null);
        Tenant tenant = null;
        if (user != null && user.getTenantId() != null) {
            tenant = tenantRepository.findById(user.getTenantId()).orElse(null);
        }

        if (tenant != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();
            file.transferTo(new File(UPLOAD_DIR + fileName));

            TenantDocument doc = new TenantDocument();
            doc.setDocumentName(documentName);
            doc.setDocumentType(documentType);
            doc.setTenantName(tenant.getName());
            doc.setApartmentNumber(tenant.getApartmentNumber());
            doc.setFileName(fileName);
            doc.setUploadDate(LocalDate.now().toString());
            doc.setUploadedBy(username);
            documentRepository.save(doc);
        }
        return "redirect:/my-documents";
    }

    @PostMapping("/documents/delete/{id}")
    public String deleteDocument(@PathVariable Long id, HttpSession session) {
        documentRepository.deleteById(id);
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role) ? "redirect:/documents" : "redirect:/my-documents";
    }
}
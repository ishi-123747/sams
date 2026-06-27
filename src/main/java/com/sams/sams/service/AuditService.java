package com.sams.sams.service;

import com.sams.sams.model.AuditLog;
import com.sams.sams.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AuditService {

    @Autowired private AuditLogRepository auditLogRepository;

    public void log(String username, String action, String module, String details) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            auditLogRepository.save(new AuditLog(username, action, module, details, timestamp));
        } catch (Exception e) {
            System.err.println("AuditService log failed: " + e.getMessage());
        }
    }
}
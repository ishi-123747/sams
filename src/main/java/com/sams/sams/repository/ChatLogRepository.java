package com.sams.sams.repository;

import com.sams.sams.model.ChatLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatLogRepository extends JpaRepository<ChatLog, Long> {
    List<ChatLog> findAllByOrderByIdDesc();
}
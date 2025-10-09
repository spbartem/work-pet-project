package ru.fkr.workpetproject.service.db;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

@Service
public class PostgresNotificationListener {

    private final DataSource dataSource;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostgresNotificationListener(DataSource dataSource, SimpMessagingTemplate messagingTemplate) {
        this.dataSource = dataSource;
        this.messagingTemplate = messagingTemplate;
    }

    @PostConstruct
    public void startListener() {
        new Thread(() -> {
            try (Connection conn = dataSource.getConnection()) {
                PGConnection pgConn = conn.unwrap(PGConnection.class);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("LISTEN address_progress");
                }

                while (true) {
                    PGNotification[] notifications = pgConn.getNotifications(5000);
                    if (notifications != null) {
                        for (PGNotification notif : notifications) {
                            handleNotification(notif.getParameter());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "pg-notify-listener").start();
    }

    private void handleNotification(String payload) {
        try {
            Map<String, Object> map = objectMapper.readValue(payload, Map.class);
            messagingTemplate.convertAndSend("/topic/address-progress", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


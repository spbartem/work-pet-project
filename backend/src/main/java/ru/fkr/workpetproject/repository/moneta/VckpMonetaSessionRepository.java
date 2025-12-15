package ru.fkr.workpetproject.repository.moneta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSession;

import java.util.Optional;

@Repository
public interface VckpMonetaSessionRepository extends JpaRepository<VckpMonetaSession, Long> {
    VckpMonetaSession findVckpMonetaSessionsByVckpMonetaSessionId(Long sessionId);
    Optional<VckpMonetaSession> findFirstByVckpMonetaSessionStatus_StatusCodeOrderByVckpMonetaSessionIdDesc(String statusCode);
    boolean existsByFileName(String fileName);
}

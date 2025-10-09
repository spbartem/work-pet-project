package ru.fkr.workpetproject.repository.moneta;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSessionStatus;

import java.util.Optional;

public interface VckpMonetaSessionStatusRepository extends JpaRepository<VckpMonetaSessionStatus, Long> {
    Optional<VckpMonetaSessionStatus> findByStatusCode(String code);
}

package ru.fkr.workpetproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.entity.RosreestrFullInfoSession;

@Repository
public interface RosreestrFullInfoSessionRepository extends JpaRepository<RosreestrFullInfoSession, Long> {
}

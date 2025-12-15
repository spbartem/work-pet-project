package ru.fkr.workpetproject.repository.moneta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fkr.workpetproject.dao.entity.VckpMoneta;

@Repository
public interface VckpMonetaRepository extends JpaRepository<VckpMoneta, Long> {
}

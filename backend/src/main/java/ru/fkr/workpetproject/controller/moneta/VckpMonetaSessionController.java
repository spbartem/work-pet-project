package ru.fkr.workpetproject.controller.moneta;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fkr.workpetproject.dao.dto.moneta.MonetaSessionDto;
import ru.fkr.workpetproject.service.moneta.MonetaService;
import ru.fkr.workpetproject.repository.moneta.VckpMonetaSessionRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/moneta/vckp-moneta-sessions")
@RequiredArgsConstructor
public class VckpMonetaSessionController {

    private final VckpMonetaSessionRepository sessionRepository;
    private final MonetaService monetaService;

    // GET /api/sessions?page=...&size=...
    @GetMapping
    public Page<MonetaSessionDto> getSessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "vckpMonetaSessionId"));
        return sessionRepository.findAll(pageable).map(MonetaSessionDto::fromEntity);
    }

    // GET /api/sessions/{id}
    @GetMapping("/{sessionId}" )
    public ResponseEntity<MonetaSessionDto> getSessionById(@PathVariable Long sessionId) {
        return sessionRepository.findById(sessionId)
                .map(MonetaSessionDto::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{sessionId}/fill-addresses")
    public ResponseEntity<?> fillAddresses(@PathVariable Long sessionId) {
        monetaService.startAddressParsing(sessionId);
        return ResponseEntity.ok(Map.of("message", "Парсинг адресов запущен"));
    }

    @PostMapping("/{sessionId}/fill-sum")
    public ResponseEntity<?> fillSum(@PathVariable Long sessionId) {
        monetaService.startFillingSum(sessionId);
        return ResponseEntity.ok(Map.of("message", "Заполнение сумм запущено"));
    }
}


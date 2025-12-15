package ru.fkr.workpetproject.service.moneta;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fkr.workpetproject.dao.dto.moneta.MonetaProgressUploadDto;
import ru.fkr.workpetproject.dao.entity.VckpMoneta;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSession;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSessionStatus;
import ru.fkr.workpetproject.parser.moneta.XmlToCoinRecordParser;
import ru.fkr.workpetproject.repository.moneta.VckpMonetaRepository;
import ru.fkr.workpetproject.repository.moneta.VckpMonetaSessionRepository;
import ru.fkr.workpetproject.repository.moneta.VckpMonetaSessionStatusRepository;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonetaImportService {

    private final VckpMonetaRepository repository;
    private final VckpMonetaSessionRepository sessionRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final VckpMonetaSessionStatusRepository vckpMonetaSessionStatusRepository;

    @Transactional
    public void processSavedFile(Long sessionId, File file) throws Exception {
        VckpMonetaSession session = sessionRepository.findById(sessionId)
                .orElseThrow();

        try (InputStream is1 = new FileInputStream(file)) {
            int total = countRepElements(new BufferedInputStream(is1));
            session.setCountRowMoneta(total);
            sessionRepository.save(session);
        }

        try (InputStream is2 = new FileInputStream(file)) {
            int[] counter = {0};
            XmlToCoinRecordParser.parse(is2, batch -> {
                List<VckpMoneta> entities = batch.stream()
                        .map(r -> r.toEntity(session))
                        .collect(Collectors.toList());
                repository.saveAll(entities);
                counter[0] += entities.size();

                double progress = (double) counter[0] / session.getCountRowMoneta();
                session.setProgress(progress);
                sessionRepository.save(session);

                String message = "Добавление записей в БД: " + counter[0] + " из " + session.getCountRowMoneta();

                messagingTemplate.convertAndSend(
                        "/topic/progress",
                        new MonetaProgressUploadDto(sessionId, progress, message));
            }, 10000);
        }

        session.setFileLoaded(true);
        session.setVckpMonetaSessionStatus(resolveStatus(vckpMonetaSessionStatusEnum.FILE_PARSING));
        sessionRepository.save(session);
        messagingTemplate.convertAndSend(
                "/topic/progress",
                new MonetaProgressUploadDto(
                    sessionId, 1.0, "✅ Обработка завершена"
                )
        );
    }

    public VckpMonetaSessionStatus resolveStatus(vckpMonetaSessionStatusEnum statusEnum) {
        return vckpMonetaSessionStatusRepository.findByStatusCode(statusEnum.name())
                .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + statusEnum));
    }

    private int countRepElements(InputStream input) throws XMLStreamException {
        int count = 0;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(input);
        while (reader.hasNext()) {
            if (reader.next() == XMLStreamConstants.START_ELEMENT && "REP".equals(reader.getLocalName())) {
                count++;
            }
        }
        return count;
    }


    public enum vckpMonetaSessionStatusEnum {
        SESSION_CREATED,
        FILE_LOADED,
        FILE_PARSING,
        ADDRESS_FILLING,
        ADDRESS_ENTERED,
        SUM_FILLING,
        SUM_ENTERED
    }
}
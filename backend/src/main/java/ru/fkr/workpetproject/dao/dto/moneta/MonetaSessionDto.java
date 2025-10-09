package ru.fkr.workpetproject.dao.dto.moneta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fkr.workpetproject.dao.entity.VckpMonetaSession;
import ru.fkr.workpetproject.utils.NumberUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaSessionDto {
    private Long id;
    private String fileName;
    private String countRowMoneta;
    private Double progress;
    private Boolean fileLoaded;
    private String statusName;
    private String statusCode;

    public static MonetaSessionDto fromEntity(VckpMonetaSession session) {
        return new MonetaSessionDto(
                session.getVckpMonetaSessionId(),
                session.getFileName(),
                NumberUtils.formatNumber(session.getCountRowMoneta()) ,
                session.getProgress(),
                session.getFileLoaded(),
                session.getVckpMonetaSessionStatus() != null ? session.getVckpMonetaSessionStatus().getStatusName() : null,
                session.getVckpMonetaSessionStatus() != null ? session.getVckpMonetaSessionStatus().getStatusCode() : null
        );
    }
}


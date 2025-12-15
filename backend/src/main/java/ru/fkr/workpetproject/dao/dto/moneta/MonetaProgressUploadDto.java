package ru.fkr.workpetproject.dao.dto.moneta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonetaProgressUploadDto {
    private Long sessionId;
    private double progress;
    private String message;
}

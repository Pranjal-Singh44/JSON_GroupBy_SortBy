package com.FreightFox.jsondatasetapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertRecordResponse {
    private String message;
    private String dataset;
    private Long recordId;
}

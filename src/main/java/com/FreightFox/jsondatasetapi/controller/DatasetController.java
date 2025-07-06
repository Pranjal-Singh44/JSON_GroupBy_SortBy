package com.FreightFox.jsondatasetapi.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.FreightFox.jsondatasetapi.dto.InsertRecordResponse;
import com.FreightFox.jsondatasetapi.dto.QueryResponse;
import com.FreightFox.jsondatasetapi.repository.DatasetRecordRepository;
//import com.FreightFox.jsondatasetapi.exception.CustomException;
import com.FreightFox.jsondatasetapi.service.DatasetService;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;
    private final DatasetRecordRepository repository;

    @PostMapping("/{datasetName}/record")
    public ResponseEntity<InsertRecordResponse> insertRecord(
            @PathVariable String datasetName,
            @RequestBody JsonNode jsonRecord
    ) {
        InsertRecordResponse response = datasetService.insertRecord(datasetName, jsonRecord);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{datasetName}/query")
    public ResponseEntity<QueryResponse> queryDataset(
            @PathVariable String datasetName,
            @RequestParam Optional<String> groupBy,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> order
    ) {
        QueryResponse response = datasetService.queryDataset(datasetName, groupBy, sortBy, order);
        return ResponseEntity.ok(response);
    }
}

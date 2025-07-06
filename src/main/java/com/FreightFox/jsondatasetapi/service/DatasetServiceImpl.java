package com.FreightFox.jsondatasetapi.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.FreightFox.jsondatasetapi.dto.InsertRecordResponse;
import com.FreightFox.jsondatasetapi.dto.QueryResponse;
import com.FreightFox.jsondatasetapi.exception.CustomException;
import com.FreightFox.jsondatasetapi.model.DatasetRecord;
import com.FreightFox.jsondatasetapi.repository.DatasetRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {

    private final DatasetRecordRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public InsertRecordResponse insertRecord(String datasetName, JsonNode recordJson) {
        DatasetRecord record = DatasetRecord.builder()
                .datasetName(datasetName)
                .jsonData(recordJson.toString())
                .createdAt(LocalDateTime.now())
                .build();

        DatasetRecord saved = repository.save(record);
        return new InsertRecordResponse("Record added successfully", datasetName, saved.getId());
    }

    @Override
    public QueryResponse queryDataset(String datasetName, Optional<String> groupByOpt, Optional<String> sortByOpt, Optional<String> orderOpt) {
    	
    	List<DatasetRecord> records = repository.findByDatasetName(datasetName);
    	if (records.isEmpty()) {
    	    throw new CustomException("Dataset '" + datasetName + "' not found", HttpStatus.NOT_FOUND);
    	}

        List<JsonNode> jsonList = records.stream()
                .map(r -> {
                    try {
                        return objectMapper.readTree(r.getJsonData());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (groupByOpt.isPresent()) {
            String groupBy = groupByOpt.get();
            Map<String, List<JsonNode>> grouped = jsonList.stream()
                    .collect(Collectors.groupingBy(j -> j.has(groupBy) ? j.get(groupBy).asText() : "undefined"));
            return QueryResponse.grouped(grouped);
        }

        if (sortByOpt.isPresent()) {
            String sortBy = sortByOpt.get();
            String order = orderOpt.orElse("asc");
            Comparator<JsonNode> comparator = Comparator.comparing(j -> j.has(sortBy) ? j.get(sortBy).asText() : "");
            if (order.equalsIgnoreCase("desc")) {
                comparator = comparator.reversed();
            }
            List<JsonNode> sorted = jsonList.stream().sorted(comparator).collect(Collectors.toList());
            return QueryResponse.sorted(sorted);
        }

        return QueryResponse.sorted(jsonList);
    }
}

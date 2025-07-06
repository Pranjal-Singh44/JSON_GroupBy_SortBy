package com.FreightFox.jsondatasetapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.FreightFox.jsondatasetapi.dto.InsertRecordResponse;
import com.FreightFox.jsondatasetapi.dto.QueryResponse;

import java.util.Optional;

public interface DatasetService {
    InsertRecordResponse insertRecord(String datasetName, JsonNode recordJson);
    QueryResponse queryDataset(String datasetName, Optional<String> groupBy, Optional<String> sortBy, Optional<String> order);
}

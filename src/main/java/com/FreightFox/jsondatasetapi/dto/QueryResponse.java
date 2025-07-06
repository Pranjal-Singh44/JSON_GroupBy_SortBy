package com.FreightFox.jsondatasetapi.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class QueryResponse {

    private Map<String, List<JsonNode>> groupedRecords;
    private List<JsonNode> sortedRecords;

    public static QueryResponse grouped(Map<String, List<JsonNode>> grouped) {
        return new QueryResponse(grouped, null);
    }

    public static QueryResponse sorted(List<JsonNode> sorted) {
        return new QueryResponse(null, sorted);
    }
}

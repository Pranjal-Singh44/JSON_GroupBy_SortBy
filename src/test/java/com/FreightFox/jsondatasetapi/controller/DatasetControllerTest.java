package com.FreightFox.jsondatasetapi.controller;

import com.FreightFox.jsondatasetapi.dto.InsertRecordResponse;
import com.FreightFox.jsondatasetapi.dto.QueryResponse;
import com.FreightFox.jsondatasetapi.service.DatasetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatasetControllerTest {

    @Mock
    private DatasetService datasetService;

    @InjectMocks
    private DatasetController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @SuppressWarnings("deprecation")
	@Test
    void testInsertRecord() {
        ObjectNode json = objectMapper.createObjectNode();
        json.put("id", 1);
        json.put("name", "John");
        json.put("age", 30);
        json.put("department", "Engineering");

        InsertRecordResponse mockResponse = new InsertRecordResponse("Record added successfully", "employees", 1L);

        when(datasetService.insertRecord("employees", json)).thenReturn(mockResponse);

        ResponseEntity<InsertRecordResponse> response = controller.insertRecord("employees", json);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Record added successfully", response.getBody().getMessage());
        assertEquals("employees", response.getBody().getDataset());
    }

    @Test
    void testQueryDataset() {
        QueryResponse mockResponse = QueryResponse.grouped(
            Map.of("Engineering", List.of(objectMapper.createObjectNode()))
        );

        when(datasetService.queryDataset(eq("employees"), any(), any(), any())).thenReturn(mockResponse);

        ResponseEntity<QueryResponse> response = controller.queryDataset("employees",
                Optional.of("department"), Optional.empty(), Optional.empty());

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().getGroupedRecords());
        assertTrue(response.getBody().getGroupedRecords().containsKey("Engineering"));
    }
}
package com.FreightFox.jsondatasetapi.service;

import com.FreightFox.jsondatasetapi.dto.InsertRecordResponse;
import com.FreightFox.jsondatasetapi.dto.QueryResponse;
import com.FreightFox.jsondatasetapi.exception.CustomException;
import com.FreightFox.jsondatasetapi.model.DatasetRecord;
import com.FreightFox.jsondatasetapi.repository.DatasetRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatasetServiceImplTest {

    @Mock
    private DatasetRecordRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DatasetServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertRecord_success() {
        ObjectNode json = new ObjectMapper().createObjectNode();
        json.put("id", 1);
        json.put("name", "John");
        json.put("age", 30);
        json.put("department", "Engineering");

        DatasetRecord savedRecord = DatasetRecord.builder()
                .id(1L)
                .datasetName("employees")
                .jsonData(json.toString())
                .createdAt(LocalDateTime.now())
                .build();

        when(repository.save(any(DatasetRecord.class))).thenReturn(savedRecord);

        InsertRecordResponse response = service.insertRecord("employees", json);

        assertNotNull(response);
        assertEquals("employees", response.getDataset());
        assertEquals("Record added successfully", response.getMessage());
        assertEquals(1L, response.getRecordId());
    }

    @Test
    void testQueryDataset_groupByDepartment() throws Exception {
        ObjectNode json1 = new ObjectMapper().createObjectNode();
        json1.put("id", 1);
        json1.put("name", "Alice");
        json1.put("age", 25);
        json1.put("department", "Engineering");

        ObjectNode json2 = new ObjectMapper().createObjectNode();
        json2.put("id", 2);
        json2.put("name", "Bob");
        json2.put("age", 30);
        json2.put("department", "Marketing");

        List<DatasetRecord> records = List.of(
                new DatasetRecord(1L, "employees", json1.toString(), LocalDateTime.now()),
                new DatasetRecord(2L, "employees", json2.toString(), LocalDateTime.now())
        );

        when(repository.findByDatasetName("employees")).thenReturn(records);
        when(objectMapper.readTree(json1.toString())).thenReturn(json1);
        when(objectMapper.readTree(json2.toString())).thenReturn(json2);

        QueryResponse response = service.queryDataset("employees",
                Optional.of("department"), Optional.empty(), Optional.empty());

        assertNotNull(response);
        assertNotNull(response.getGroupedRecords());
        assertTrue(response.getGroupedRecords().containsKey("Engineering"));
        assertTrue(response.getGroupedRecords().containsKey("Marketing"));
    }

    @Test
    void testQueryDataset_sortByAgeAsc() throws Exception {
        ObjectNode json1 = new ObjectMapper().createObjectNode();
        json1.put("id", 1);
        json1.put("name", "Alice");
        json1.put("age", 25);
        json1.put("department", "Engineering");

        ObjectNode json2 = new ObjectMapper().createObjectNode();
        json2.put("id", 2);
        json2.put("name", "Bob");
        json2.put("age", 30);
        json2.put("department", "Engineering");

        List<DatasetRecord> records = List.of(
                new DatasetRecord(1L, "employees", json2.toString(), LocalDateTime.now()),
                new DatasetRecord(2L, "employees", json1.toString(), LocalDateTime.now())
        );

        when(repository.findByDatasetName("employees")).thenReturn(records);
        when(objectMapper.readTree(json1.toString())).thenReturn(json1);
        when(objectMapper.readTree(json2.toString())).thenReturn(json2);

        QueryResponse response = service.queryDataset("employees",
                Optional.empty(), Optional.of("age"), Optional.of("asc"));

        assertNotNull(response);
        assertNotNull(response.getSortedRecords());
        assertEquals(25, response.getSortedRecords().get(0).get("age").asInt());
        assertEquals(30, response.getSortedRecords().get(1).get("age").asInt());
    }

    @Test
    void testQueryDataset_emptyDataset_throwsException() {
        when(repository.findByDatasetName("no_dataset")).thenReturn(Collections.emptyList());

        CustomException ex = assertThrows(CustomException.class, () ->
                service.queryDataset("no_dataset", Optional.empty(), Optional.empty(), Optional.empty()));

        assertEquals("Dataset 'no_dataset' not found", ex.getMessage());
    }
}

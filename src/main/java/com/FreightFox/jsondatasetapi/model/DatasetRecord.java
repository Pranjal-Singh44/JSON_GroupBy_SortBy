package com.FreightFox.jsondatasetapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "dataset_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatasetRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String datasetName;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String jsonData;

    private LocalDateTime createdAt;
}

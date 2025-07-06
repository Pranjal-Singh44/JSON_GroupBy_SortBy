package com.FreightFox.jsondatasetapi.repository;

import com.FreightFox.jsondatasetapi.model.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatasetRecordRepository extends JpaRepository<DatasetRecord, Long> {
    List<DatasetRecord> findByDatasetName(String datasetName);
}

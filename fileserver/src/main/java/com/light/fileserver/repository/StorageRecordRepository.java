package com.light.fileserver.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import com.light.fileserver.model.StorageRecord;

/**
 * StorageRecordRepository
 */
public interface StorageRecordRepository extends Repository<StorageRecord, Long> {

    Page<StorageRecord> findAll(Pageable pageable);

    StorageRecord findById(Long id);

    StorageRecord save(StorageRecord model);

    void deleteById(Long id);

    List<StorageRecord> findAllByIdIn(List<Long> ids);

}

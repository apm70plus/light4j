package com.light.fileserver.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.light.fileserver.model.StorageRecord;
import com.light.fileserver.repository.StorageRecordRepository;
import com.light.fileserver.service.StorageRecordService;
import com.light.identity.DefaultIdGenerator;
import com.light.identity.IdGenerator;

import lombok.NonNull;

/**
 * StorageRecordService 实现类
 */
@Service
@Transactional
public class StorageRecordServiceImpl implements StorageRecordService {

	private IdGenerator idGenerator = new DefaultIdGenerator(1, "s");
    @Autowired
    private StorageRecordRepository storageRecordRepository;

    @Override
    public StorageRecord getStorageRecord(@NonNull final Long id) {
        final StorageRecord model = this.storageRecordRepository.findById(id);
        if (model == null) {
        	throw NotFoundException.of(String.format("查找的资源[%s]不存在.", id));
        }
        return model;
    }

    @Override
    public StorageRecord createStorageRecord(final StorageRecord model) {
    	model.setId(idGenerator.generate());
        return this.storageRecordRepository.save(model);
    }

    @Override
    public StorageRecord updateStorageRecord(final StorageRecord model) {
        return this.storageRecordRepository.save(model);
    }

    @Override
    public void deleteStorageRecord(@NonNull final Long id) {
        this.storageRecordRepository.deleteById(id);
    }

    @Override
    public List<StorageRecord> getStorageRecords(final List<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return this.storageRecordRepository.findAllByIdIn(ids);
    }
}

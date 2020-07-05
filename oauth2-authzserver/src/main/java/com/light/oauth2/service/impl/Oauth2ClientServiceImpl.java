package com.light.oauth2.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.light.exception.NotFoundException;
import com.light.identity.DefaultIdGenerator;
import com.light.identity.IdGenerator;
import com.light.oauth2.model.Oauth2Client;
import com.light.oauth2.repository.Oauth2ClientRepository;
import com.light.oauth2.service.Oauth2ClientService;

import lombok.NonNull;

/**
 * Oauth2ClientService 实现类
 */
@Service
@Transactional
public class Oauth2ClientServiceImpl implements Oauth2ClientService {

	private IdGenerator uuidGenerator = new DefaultIdGenerator(1, "C");
    @Autowired
    private Oauth2ClientRepository oauth2ClientRepository;

    @Override
    @Transactional(readOnly = true)
    public Oauth2Client get(@NonNull Long id) {
        final  Optional<Oauth2Client> model = oauth2ClientRepository.findById(id);
        return model.orElseThrow(() -> new NotFoundException(String.format("查找的资源[%s]不存在.", id)));
    }

    @Override
    public Oauth2Client create(Oauth2Client model) {
        model.setClientId(uuidGenerator.generateCode());
        model.setClientSecret(uuidGenerator.generateNonceToken());
        return oauth2ClientRepository.save(model);
    }

    @Override
    public Oauth2Client update(Oauth2Client model) {
        return oauth2ClientRepository.save(model);
    }

    @Override
    public void delete(@NonNull Long id) {
        oauth2ClientRepository.deleteById(id);
    }
}

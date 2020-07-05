package com.light.oauth2.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.light.oauth2.dto.Oauth2ClientDTO;
import com.light.oauth2.model.Oauth2Client;
import com.light.oauth2.service.Oauth2ClientService;
import com.light.web.dto.AbstractConvertor;

import lombok.NonNull;

/**
 * Oauth2ClientConvertor
 */
@Component
public class Oauth2ClientConvertor extends AbstractConvertor<Oauth2Client, Oauth2ClientDTO> {

    @Autowired
    private Oauth2ClientService oauth2ClientService;
    
    @Override
    public Oauth2Client toModel(@NonNull final Oauth2ClientDTO dto) {
        if (dto.isNew()) {//新增
            return constructModel(dto);
        } else {//更新
            return updateModel(dto);
        }
    }

    @Override
    public Oauth2ClientDTO toDTO(@NonNull final Oauth2Client model, final boolean forListView) {
        final Oauth2ClientDTO dto = new Oauth2ClientDTO();
        dto.setId(model.getId());
        dto.setClientName(model.getClientName());
        dto.setClientId(model.getClientId());
        dto.setClientSecret(model.getClientSecret());
        dto.setScope(model.getScope());

        return dto;
    }

    // 构建新Model
    private Oauth2Client constructModel(final Oauth2ClientDTO dto) {
        Oauth2Client model = new Oauth2Client();
        model.setClientName(dto.getClientName());
        model.setClientId(dto.getClientId());
        model.setClientSecret(dto.getClientSecret());
        model.setScope(dto.getScope());

        return model;
    }

    // 更新Model
    private Oauth2Client updateModel(final Oauth2ClientDTO dto) {
        Oauth2Client model = oauth2ClientService.get(dto.getId());
        model.setClientName(dto.getClientName());
        model.setScope(dto.getScope());
        //model.setClientId(dto.getClientId());
        //model.setClientSecret(dto.getClientSecret());

        return model;
    }
}

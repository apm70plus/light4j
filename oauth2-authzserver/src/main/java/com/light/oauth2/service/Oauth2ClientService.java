package com.light.oauth2.service;

import com.light.oauth2.model.Oauth2Client;

/**
 * Oauth2ClientService
 */
public interface Oauth2ClientService {

    /**
     * 根据ID获取资源
     *
     * @param id 资源实例ID
     * @return Id所指向的资源实例
     * @throws 当Id所指向的资源不存在时，抛CustomRuntimeException异常
     */
    Oauth2Client get(Long id);

    /**
     * 创建
     *
     * @param model 资源实例
     * @return 创建后的对象
     */
    Oauth2Client create(Oauth2Client model);

    /**
     * 更新
     *
     * @param model 编辑后的资源实例
     * @return 修改后的对象
     */
    Oauth2Client update(Oauth2Client model);
    
    /**
     * 删除
     *
     * @param id 资源实例ID
     */
    void delete(Long id);

}

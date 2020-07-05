package com.light.kaptcha.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.light.kaptcha.KaptchaService;
import com.light.kaptcha.SmsCaptchaService;
import com.light.util.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信验证码服务的阿里云实现
 * @author liuyg
 */
@Slf4j
public class AliSmsCaptchaServiceImpl implements SmsCaptchaService {

	@Value("${framework.kaptcha.sms.aliyun.accessKeyId}")
	private String accessKeyId;
	@Value("${framework.kaptcha.sms.aliyun.accessSecret}")
	private String accessSecret;
	@Value("${framework.kaptcha.sms.aliyun.signName}")
	private String signName;
	@Autowired
	private KaptchaService kaptchaService;
	
	@Override
	public Response sendSmsCaptcha(String mobile, String scope, String template) {
		String captcha = kaptchaService.createSmsCaptcha(mobile, scope);
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        Map<String, String> params = new HashMap<>();
        params.put("code", captcha);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "智能互联");
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", JsonUtils.pojoToJson(params));
        
        Response rs = new Response();
        try {
            CommonResponse response = client.getCommonResponse(request);
            rs.setData(response.getData());
            rs.setHttpStatus(response.getHttpStatus());
        } catch (ServerException e) {
            rs.setHttpStatus(500);
            log.error("发送短信验证码失败", e);
        } catch (ClientException e) {
        	rs.setHttpStatus(500);
        	log.error("发送短信验证码失败", e);
        }
        return rs;
	}

	@Override
	public boolean validSmsCaptcha(String mobile, String scope, String smsCaptcha) {
		return kaptchaService.validCaptcha(mobile, scope, smsCaptcha);
	}
}

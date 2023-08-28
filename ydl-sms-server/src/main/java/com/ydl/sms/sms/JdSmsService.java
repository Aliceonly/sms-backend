package com.ydl.sms.sms;

import com.alibaba.fastjson.JSON;
import com.ydl.sms.entity.SmsConfig;
import com.ydl.sms.entity.TemplateEntity;
import com.ydl.sms.utils.StringHelper;
import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 京东
 * https://webv2.lx198.com/index.html
 */
@Slf4j
public class JdSmsService extends AbstractSmsService {

    private SmsClient smsClient;
    // 地域信息不用修改
    private String region = "cn-north-1";

    public JdSmsService(SmsConfig config) {
        this.config = config;
        //初始化
        init();
    }

    private void init() {
        //初始化acsClient，暂不支持region化 "cn-hangzhou"
        /**
         * 普通用户ak/sk （应用管理-文本短信-概览 页面可以查看自己ak/sk）
         */
        // 请填写AccessKey ID
        String accessKeyId = config.getAccessKeyId();
        // 请填写AccessKey Secret
        String secretAccessKey = config.getAccessKeySecret();
        CredentialsProvider credentialsProvider = new StaticCredentialsProvider(accessKeyId, secretAccessKey);
        smsClient = SmsClient.builder()
                .credentialsProvider(credentialsProvider)
                .httpRequestConfig(new HttpRequestConfig.Builder().protocol(Protocol.HTTP).build()) //默认为HTTPS
                .build();
    }

    @Override
    protected String sendSms(String mobile, Map<String, String> params, String signature, String template) {
        // 获取 签名内容 和模板id
        String signatureCode = signatureService.getConfigCodeByCode(config.getId(), signature);
        String templateCode = templateService.getConfigCodeByCode(config.getId(), template);
        TemplateEntity templateEntity = templateService.getByCode(template);

        BatchSendRequest request = new BatchSendRequest();
        request.setRegionId(region);
        // 设置模板ID 应用管理-文本短信-短信模板 页面可以查看模板ID
        request.setTemplateId(templateCode);
        // 设置签名ID 应用管理-文本短信-短信签名 页面可以查看签名ID
        request.setSignId(signatureCode);
        // 设置下发手机号list
        List<String> phoneList = new ArrayList<>();
        phoneList.add(mobile);
        request.setPhoneList(phoneList);
        // 设置模板参数，非必传，如果模板中包含变量请填写对应参数，否则变量信息将不做替换。

        List<String> requestParams = new ArrayList<>();
        List<String> paramsField = StringHelper.getSubUtil(templateEntity.getContent());
        if (!CollectionUtils.isEmpty(paramsField)) {
            for (String field : paramsField) {
                requestParams.add(params.get(field));
            }
        }
        request.setParams(requestParams);
        BatchSendResponse response = smsClient.batchSend(request);
        String json = JSON.toJSONString(response);
        if (200 == response.getJdcloudHttpResponse().getStatusCode()) {
            if (!response.getResult().getStatus()) {
                return failResponse(response.getResult().getMessage(), json);
            }
        } else {
            return failResponse(response.getJdcloudHttpResponse().getStatusMessage(), json);
        }
        return json;
    }

    @Override
    protected String sendSmsBatch(String[] mobiles, LinkedHashMap<String, String>[] params, String[] signatures, String[] templates) {

        return null;
    }
}

package com.ydl.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydl.sms.entity.TemplateEntity;

/**
 * 模板表
 *
 * @author Aliceonly
 *
 */
public interface TemplateService extends IService<TemplateEntity> {

    TemplateEntity getByCode(String template);
}

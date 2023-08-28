package com.ydl.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydl.sms.entity.SignatureEntity;

/**
 * 签名表
 *
 * @author Aliceonly
 *
 */
public interface SignatureService extends IService<SignatureEntity> {

    SignatureEntity getByCode(String signature);
}

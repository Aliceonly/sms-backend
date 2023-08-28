package com.ydl.sms.dto;

import com.ydl.sms.entity.ReceiveLogEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 接收日志表
 *
 * @author Aliceonly
 */
@Data
@ApiModel(description = "接收日志表")
public class ReceiveLogDTO extends ReceiveLogEntity {

}
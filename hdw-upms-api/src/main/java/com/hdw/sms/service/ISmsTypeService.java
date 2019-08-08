package com.hdw.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageParams;
import com.hdw.sms.entity.SmsType;


/**
 * 消息类型与用户关系表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
public interface ISmsTypeService extends IService<SmsType> {

    PageParams selectDataGrid(PageParams pageParams);


}


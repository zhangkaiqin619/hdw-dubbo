package com.hdw.sms.service;


import com.hdw.common.mybatis.base.service.IBaseService;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SmsType;
import com.hdw.sms.dto.SmsTypeDTO;


/**
 * 消息类型与用户关系表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
public interface ISmsTypeService extends IBaseService<SmsType> {

    /**
     * 自定义分页
     * @param smsTypeDTO
     * @return
     */
    PageVo selectSmsTypePageList(SmsTypeDTO smsTypeDTO);

}


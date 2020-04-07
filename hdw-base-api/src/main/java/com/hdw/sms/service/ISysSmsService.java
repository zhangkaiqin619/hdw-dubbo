package com.hdw.sms.service;

import com.hdw.common.mybatis.base.service.IBaseService;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SysSms;
import com.hdw.sms.dto.SmsDTO;

import java.util.List;


/**
 * 消息表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
public interface ISysSmsService extends IBaseService<SysSms> {

    /**
     * 自定义分页
     * @param smsDTO
     * @return
     */
    PageVo selectSmsPageList(SmsDTO smsDTO);

    /**
     * 获取待推送消息
     *
     * @return
     */
    List<SysSms> selectCurrentSmsList();

}


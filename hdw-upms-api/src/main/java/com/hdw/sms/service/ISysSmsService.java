package com.hdw.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.result.PageParams;
import com.hdw.sms.entity.SysSms;

import java.util.List;


/**
 * 消息表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
public interface ISysSmsService extends IService<SysSms> {

    PageParams selectDataGrid(PageParams pageParams);

    /**
     * 获取待推送消息
     * @return
     */
    List<SysSms> selectCurrentSmsList();

}


package com.hdw.sms.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hdw.sms.entity.SysSms;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @Description 消息表
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
public interface SysSmsMapper extends BaseMapper<SysSms> {

    /**
     * 自定义分页
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    IPage<SysSms> selectPageList(IPage<SysSms> page, @Param("ew") Wrapper<SysSms> queryWrapper);

    /**
     * 获取待推送消息
     * @return
     */
    List<SysSms> selectCurrentSmsList();

}

package com.hdw.common.mybatis.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hdw.common.mybatis.base.dto.CommonDTO;
import com.hdw.common.mybatis.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * @Description com.hdw.common.mybatis.base.service
 * @Author TuMingLong
 * @Date 2019/11/7 14:34
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 自定义分页
     *
     * @param commonDTO
     * @return
     */
    PageVo pageList(CommonDTO commonDTO);

    /**
     * 自定义查询
     *
     * @param commonDTO
     * @return
     */
    List<Map<String, Object>> selectMapList(CommonDTO commonDTO);
}

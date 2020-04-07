package com.hdw.common.mybatis.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdw.common.mybatis.base.dto.CommonDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description com.hdw.common.mybatis.base.mapper
 * @Author TuMingLong
 * @Date 2019/11/7 15:00
 */
public interface SuperMapper<T> extends BaseMapper<T> {

    /**
     * 自定义分页
     *
     * @param page
     * @param commonDTO
     * @return
     */
    IPage<T> pageList(Page<T> page, @Param("ew") CommonDTO commonDTO);

    /**
     * 自定义查询
     *
     * @param commonDTO
     * @return
     */
    List<Map<String, Object>> selectMapList(@Param("ew") CommonDTO commonDTO);
}

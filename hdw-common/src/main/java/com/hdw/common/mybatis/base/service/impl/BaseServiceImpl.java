package com.hdw.common.mybatis.base.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.mybatis.base.dto.CommonDTO;
import com.hdw.common.mybatis.base.mapper.SuperMapper;
import com.hdw.common.mybatis.base.service.IBaseService;
import com.hdw.common.mybatis.base.vo.PageVo;

import java.util.List;
import java.util.Map;

/**
 * @Description com.hdw.common.mybatis.base.service.impl
 * @Author TuMingLong
 * @Date 2019/11/7 14:35
 */
public abstract class BaseServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    public PageVo pageList(CommonDTO commonDTO) {
        Page page = new Page();
        // 设置当前页码
        page.setCurrent(commonDTO.getPage());
        // 设置页大小
        page.setSize(commonDTO.getLimit());
        IPage iPage = this.baseMapper.pageList(page, commonDTO);
        return new PageVo(iPage);

    }

    public List<Map<String, Object>> selectMapList(CommonDTO commonDTO) {
        return this.baseMapper.selectMapList(commonDTO);
    }


}

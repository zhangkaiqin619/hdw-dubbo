package com.hdw.upms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hdw.common.result.PageParams;
import com.hdw.upms.entity.SysFile;
import com.hdw.upms.mapper.SysFileMapper;
import com.hdw.upms.service.ISysFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 附件表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Override
    public List<SysFile> selectFileListByTableIdAndRecordId(Map<String, Object> params) {
        return this.baseMapper.selectFileListByTableIdAndRecordId(params);
    }

    @Override
    public PageParams selectSysFilePage(Map<String, Object> params) {
        PageParams pageParams = new PageParams(params);
        IPage<SysFile> iPage = this.baseMapper.selectSysFilePage(pageParams, pageParams.getRequestMap());
        return new PageParams(iPage);
    }
}

package com.hdw.sms.controller;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.hdw.common.api.CommonResult;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.sms.entity.SysSms;
import com.hdw.sms.dto.SmsDTO;
import com.hdw.sms.service.ISysSmsService;
import com.hdw.shiro.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * 消息表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Api(value = "消息表接口", tags = {"消息表接口"})
@RestController
@RequestMapping("sms/sms")
public class SmsController {
    @Reference
    private ISysSmsService smsService;

    /**
     * 列表
     */
    @ApiOperation(value = "消息表列表", notes = "消息表列表")
    @GetMapping("/list")
    @RequiresPermissions("sms/sms/list")
    public CommonResult<PageVo<SysSms>> list(SmsDTO smsDTO) {
        PageVo<SysSms> page = smsService.selectSmsPageList(smsDTO);
        return CommonResult.success(page);
    }


    /**
     * 消息信息
     */
    @ApiOperation(value = "消息信息", notes = "消息信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键ID", dataType = "Integer", required = true)
    @GetMapping("/info/{id}")
    @RequiresPermissions("sms/sms/info")
    public CommonResult<SysSms> info(@PathVariable("id") Long id) {
        SysSms sysSms = smsService.getById(id);
        return CommonResult.success(sysSms);
    }

    /**
     * 保存消息信息
     */
    @ApiOperation(value = "保存消息信息", notes = "保存消息信息")
    @PostMapping("/save")
    @RequiresPermissions("sms/sms/save")
    public CommonResult save(@Valid @RequestBody SysSms sysSms) {
        try {
            Snowflake snowflake = IdUtil.createSnowflake(1, 1);
            long id = snowflake.nextId();
            sysSms.setId(id);
            sysSms.setCreateTime(new Date());
            sysSms.setCreateUser(ShiroUtil.getUser().getId());
            sysSms.setUpdateTime(new Date());
            sysSms.setUpdateUser(ShiroUtil.getUser().getId());
            smsService.save(sysSms);
            return CommonResult.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }
    }

    /**
     * 修改消息信息
     */
    @ApiOperation(value = "修改消息信息", notes = "修改消息信息")
    @PostMapping("/update")
    @RequiresPermissions("sms/sms/update")
    public CommonResult update(@Valid @RequestBody SysSms sysSms) {
        try {
            sysSms.setUpdateUser(ShiroUtil.getUser().getId());
            sysSms.setUpdateTime(new Date());
            smsService.updateById(sysSms);
            return CommonResult.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }

    }

    /**
     * 删除消息信息
     */
    @ApiOperation(value = "删除消息信息", notes = "删除消息信息")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "主键ID数组", dataType = "Integer", required = true, allowMultiple = true)
    @PostMapping("/delete")
    @RequiresPermissions("sms/sms/delete")
    public CommonResult delete(@RequestBody Long[] ids) {
        try {
            smsService.removeByIds(Arrays.asList(ids));
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }
    }

}

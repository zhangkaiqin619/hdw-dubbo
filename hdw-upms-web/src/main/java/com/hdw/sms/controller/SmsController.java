package com.hdw.sms.controller;

import com.hdw.common.base.controller.BaseController;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.sms.entity.SysSms;
import com.hdw.sms.service.ISysSmsService;
import com.hdw.upms.shiro.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 消息表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Api(value = "消息表接口", tags = {"消息表接口"})
@RestController
@RequestMapping("sms/sms")
public class SmsController extends BaseController {
    @Reference
    private ISysSmsService smsService;

    /**
     * 列表
     */
    @ApiOperation(value = "消息表列表", notes = "消息表列表")
    @GetMapping("/list")
    @RequiresPermissions("sms/sms/list")
    public Object list(@RequestParam Map<String, Object> params) {
        PageParams page = smsService.selectDataGrid(new PageParams(params));
        return ResultMap.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取消息表", notes = "获取消息表")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sms/sms/info")
    public Object info(@PathVariable("id") Long id) {
        SysSms sysSms = smsService.getById(id);
        return ResultMap.ok().put("sms", sysSms);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存消息表", notes = "保存消息表")
    @PostMapping("/save")
    @RequiresPermissions("sms/sms/save")
    public Object save(@Valid @RequestBody SysSms sysSms) {
        try {
            sysSms.setCreateTime(new Date());
            sysSms.setCreateUser(ShiroKit.getUser().getId());
            sysSms.setUpdateTime(new Date());
            sysSms.setUpdateUser(ShiroKit.getUser().getId());
            smsService.save(sysSms);
            return ResultMap.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改消息表", notes = "修改消息表")
    @PostMapping("/update")
    @RequiresPermissions("sms/sms/update")
    public Object update(@Valid @RequestBody SysSms sysSms) {
        try {
            sysSms.setUpdateUser(ShiroKit.getUser().getId());
            sysSms.setUpdateTime(new Date());
            smsService.updateById(sysSms);
            return ResultMap.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }

    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除消息表", notes = "删除消息表")
    @PostMapping("/delete")
    @RequiresPermissions("sms/sms/delete")
    public Object delete(@RequestBody Long[] ids) {
        try {
            smsService.removeByIds(Arrays.asList(ids));
            return ResultMap.ok("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

}

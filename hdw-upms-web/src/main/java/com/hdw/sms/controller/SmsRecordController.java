package com.hdw.sms.controller;

import com.hdw.common.base.controller.BaseController;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.sms.service.ISmsRecordService;
import com.hdw.sms.service.ISmsTypeService;
import com.hdw.sms.service.ISysSmsService;
import com.hdw.upms.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 消息记录表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Api(value = "消息记录表接口", tags = {"消息记录表接口"})
@RestController
@RequestMapping("sms/smsRecord")
public class SmsRecordController extends BaseController {
    @Reference
    private ISmsRecordService smsRecordService;

    @Reference
    private ISysSmsService smsService;

    @Reference
    private ISmsTypeService smsTypeService;

    @Reference
    private ISysUserService userService;

    /**
     * 列表
     */
    @ApiOperation(value = "消息记录表列表", notes = "消息记录表列表")
    @GetMapping("/list")
    @RequiresPermissions("sms/smsRecord/list")
    public Object list(@RequestParam Map<String, Object> params) {
        PageParams page = smsRecordService.selectDataGrid(new PageParams(params));
        return ResultMap.ok().put("page", page);
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除消息记录表", notes = "删除消息记录表")
    @PostMapping("/delete")
    @RequiresPermissions("sms/smsRecord/delete")
    public Object delete(@RequestBody Long[] ids) {
        try {
            smsRecordService.removeByIds(Arrays.asList(ids));
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 更新状态已读
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "更新状态已读", notes = "更新状态已读")
    @PostMapping("/updateStatus")
    public Object updateStatus(@RequestBody String[] ids) {
        try {
            smsRecordService.updateMessageStatus(ids);
            return ResultMap.ok("更新状态成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }
}

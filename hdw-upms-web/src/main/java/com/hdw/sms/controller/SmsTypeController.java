package com.hdw.sms.controller;

import com.hdw.common.base.controller.BaseController;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.SelectNode;
import com.hdw.sms.entity.SmsType;
import com.hdw.sms.service.ISmsTypeService;
import com.hdw.upms.entity.SysUser;
import com.hdw.upms.service.ISysUserService;
import com.hdw.upms.shiro.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 * 消息类型与用户关系表
 *
 * @Author TuMinglong
 * @Date 2019-07-31 16:31:12
 */
@Api(value = "消息类型与用户关系表接口", tags = {"消息类型与用户关系表接口"})
@RestController
@RequestMapping("sms/smsType")
public class SmsTypeController extends BaseController {
    @Reference
    private ISmsTypeService smsTypeService;

    @Reference
    private ISysUserService userService;

    /**
     * 列表
     */
    @ApiOperation(value = "消息类型与用户关系表列表", notes = "消息类型与用户关系表列表")
    @GetMapping("/list")
    @RequiresPermissions("sms/smsType/list")
    public Object list(@RequestParam Map<String, Object> params) {
        List<SmsType> list = new ArrayList<>();
        PageParams iPage = smsTypeService.selectDataGrid(new PageParams(params));
        List<SmsType> smsTypeList = iPage.getList();
        if (!smsTypeList.isEmpty()) {
            smsTypeList.forEach(smsType -> {
                if (StringUtils.isNotBlank(smsType.getTargetList())) {
                    if (smsType.getTargetList().contains(",")) {
                        String[] targetList = smsType.getTargetList().split(",");
                        smsType.setAccountList(Arrays.asList(targetList));
                        List<SysUser> userList = (List<SysUser>) userService.listByIds(Arrays.asList(targetList));
                        if (!userList.isEmpty()) {
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < userList.size(); i++) {
                                if (i == userList.size() - 1) {
                                    sb.append(userList.get(i).getName());
                                } else {
                                    sb.append(userList.get(i).getName());
                                    sb.append(",");
                                }
                            }
                            smsType.setTargetList(sb.toString());
                        }
                    } else {
                        SysUser user = userService.getById(smsType.getTargetList());
                        if (null != user) {
                            smsType.setTargetList(user.getName());
                            List<String> tempList = new ArrayList<>();
                            tempList.add(user.getId().toString());
                            smsType.setAccountList(tempList);
                        }
                    }
                }
                list.add(smsType);
            });
        }
        iPage.getList().clear();
        iPage.setList(list);
        return ResultMap.ok().put("page", iPage);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取消息类型与用户关系表", notes = "获取消息类型与用户关系表")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sms/smsType/info")
    public Object info(@PathVariable("id") Long id) {
        SmsType smsType = smsTypeService.getById(id);
        smsType.setAccountList(Arrays.asList(smsType.getTargetList()));
        return ResultMap.ok().put("smsType", smsType);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存消息类型与用户关系表", notes = "保存消息类型与用户关系表")
    @PostMapping("/save")
    @RequiresPermissions("sms/smsType/save")
    public Object save(@Valid @RequestBody SmsType smsType) {
        try {
            smsType.setCreateTime(new Date());
            smsType.setCreateUser(ShiroKit.getUser().getId());
            String targetList = StringUtils.join(smsType.getAccountList(), ",");
            smsType.setTargetList(targetList);
            smsTypeService.save(smsType);
            return ResultMap.ok("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改消息类型与用户关系表", notes = "修改消息类型与用户关系表")
    @PostMapping("/update")
    @RequiresPermissions("sms/smsType/update")
    public Object update(@Valid @RequestBody SmsType smsType) {
        try {
            smsType.setUpdateUser(ShiroKit.getUser().getId());
            smsType.setUpdateTime(new Date());
            String targetList = StringUtils.join(smsType.getAccountList(), ",");
            smsType.setTargetList(targetList);
            smsTypeService.updateById(smsType);
            return ResultMap.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }

    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除消息类型与用户关系表", notes = "删除消息类型与用户关系表")
    @PostMapping("/delete")
    @RequiresPermissions("sms/smsType/delete")
    public Object delete(@RequestBody Long[] ids) {
        try {
            smsTypeService.removeByIds(Arrays.asList(ids));
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 获取消息类型树
     *
     * @return
     */
    @ApiOperation(value = "获取消息类型树", notes = "获取消息类型树")
    @GetMapping("/getSmsTypeTree")
    public Object getSmsTypeTree() {
        try {
            List<SelectNode> nodeList = new ArrayList<>();
            List<SmsType> list = smsTypeService.list();
            list.forEach(baseUser -> {
                SelectNode node = new SelectNode();
                node.setLabel(baseUser.getTypeName());
                node.setValue(baseUser.getId().toString());
                nodeList.add(node);
            });
            return ResultMap.ok().put("list", nodeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }
}

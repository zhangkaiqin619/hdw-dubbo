package com.hdw.upms.controller;

import com.hdw.common.base.controller.BaseController;
import com.hdw.common.constants.CommonConstants;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.TreeNode;
import com.hdw.common.validator.ValidatorUtils;
import com.hdw.upms.entity.SysRole;
import com.hdw.upms.entity.SysRoleResource;
import com.hdw.upms.service.ISysRoleResourceService;
import com.hdw.upms.service.ISysRoleService;
import com.hdw.upms.shiro.ShiroKit;
import io.swagger.annotations.Api;
import org.apache.commons.compress.utils.Lists;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 角色管理
 * @Author TuMinglong
 * @Date 2018/12/13 15:12
 */
@Api(value = "角色管理接口", tags = {" 角色管理接口"})
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController {

    @Reference
    private ISysRoleService sysRoleService;
    @Reference
    private ISysRoleResourceService sysRoleResourceService;

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys/role/list")
    public ResultMap list(@RequestParam Map<String, Object> params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (ShiroKit.getUser().getId() != CommonConstants.SUPER_ADMIN) {
            params.put("createUserId", ShiroKit.getUser().getId());
        }
        PageParams page = sysRoleService.selectDataGrid(params);
        return ResultMap.ok().put("page", page);
    }

    /**
     * 角色列表
     */
    @GetMapping("/select")
    @RequiresPermissions("sys/role/select")
    public ResultMap select() {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if (ShiroKit.getUser().getId() != CommonConstants.SUPER_ADMIN) {
            map.put("createUserId", ShiroKit.getUser().getId());
        }
        List<SysRole> list = sysRoleService.selectSysRoleList(map);

        return ResultMap.ok().put("list", list);
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys/role/info")
    public ResultMap info(@PathVariable("roleId") Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        //查询角色对应的菜单
        List<Long> resourceIdList = sysRoleResourceService.selectResourceIdListByRoleId(roleId);
        role.setResourceIdList(resourceIdList);
        List<SysRoleResource> roleResourceList=sysRoleResourceService.selectResourceNodeListByRoleId(roleId);
        List<TreeNode> treeNodeList= Lists.newArrayList();
        if(!roleResourceList.isEmpty()){
            roleResourceList.forEach(roleResource ->{
                TreeNode treeNode=new TreeNode();
                treeNode.setId(roleResource.getResourceId().toString());
                treeNode.setLabel(roleResource.getResource().getName());
                treeNodeList.add(treeNode);
            });
        }
        role.setResourceNodeList(treeNodeList);
        return ResultMap.ok().put("role", role);
    }

    /**
     * 保存角色
     */
    @PostMapping("/save")
    @RequiresPermissions("sys/role/save")
    public ResultMap save(@RequestBody SysRole role) {
        ValidatorUtils.validateEntity(role);
        role.setCreateTime(new Date());
        role.setCreateUserId(ShiroKit.getUser().getId());
        sysRoleService.saveByVo(role);

        return ResultMap.ok();
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    @RequiresPermissions("sys/role/update")
    public ResultMap update(@RequestBody SysRole role) {
        ValidatorUtils.validateEntity(role);
        role.setUpdateTime(new Date());
        role.setCreateUserId(ShiroKit.getUser().getId());
        sysRoleService.updateByVo(role);

        return ResultMap.ok();
    }

    /**
     * 删除角色
     */
    @PostMapping("/delete")
    @RequiresPermissions("sys/role/delete")
    public ResultMap delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return ResultMap.ok();
    }

}

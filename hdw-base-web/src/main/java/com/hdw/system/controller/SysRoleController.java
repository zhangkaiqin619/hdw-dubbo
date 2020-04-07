package com.hdw.system.controller;

import com.hdw.common.api.CommonResult;
import com.hdw.common.constant.CommonConstant;
import com.hdw.common.model.TreeNode;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.common.validator.ValidatorUtil;
import com.hdw.system.entity.SysRole;
import com.hdw.system.entity.SysRoleResource;
import com.hdw.system.dto.RoleDTO;
import com.hdw.system.service.ISysRoleResourceService;
import com.hdw.system.service.ISysRoleService;
import com.hdw.shiro.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
public class SysRoleController {

    @Reference
    private ISysRoleService sysRoleService;
    @Reference
    private ISysRoleResourceService sysRoleResourceService;

    /**
     * 角色列表
     */
    @ApiOperation(value = "角色列表", notes = "角色列表")
    @GetMapping("/list")
    @RequiresPermissions("sys/role/list")
    public CommonResult<PageVo<SysRole>> list(RoleDTO roleDTO) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if (ShiroUtil.getUser().getId() != CommonConstant.SUPER_ADMIN) {
            roleDTO.setCreateUserId(ShiroUtil.getUser().getId());
        }
        PageVo<SysRole> page = sysRoleService.pageList(roleDTO);
        return CommonResult.success(page);
    }

    /**
     * 角色选择列表
     */
    @ApiOperation(value = "角色选择列表", notes = "角色选择列表")
    @GetMapping("/select")
    @RequiresPermissions("sys/role/select")
    public CommonResult<List<SysRole>> select() {
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if (ShiroUtil.getUser().getId() != CommonConstant.SUPER_ADMIN) {
            map.put("createUserId", ShiroUtil.getUser().getId());
        }
        List<SysRole> list = sysRoleService.selectSysRoleList(map);

        return CommonResult.success(list);
    }

    /**
     * 角色信息
     */
    @ApiOperation(value = "角色信息", notes = "角色信息")
    @ApiImplicitParam(paramType = "path", name = "roleId", value = "主键ID", dataType = "Integer", required = true)
    @GetMapping("/info/{roleId}")
    @RequiresPermissions("sys/role/info")
    public CommonResult<SysRole> info(@PathVariable("roleId") Long roleId) {
        SysRole role = sysRoleService.getById(roleId);
        //查询角色对应的菜单
        List<Long> resourceIdList = sysRoleResourceService.selectResourceIdListByRoleId(roleId);
        role.setResourceIdList(resourceIdList);
        List<SysRoleResource> roleResourceList = sysRoleResourceService.selectResourceNodeListByRoleId(roleId);
        List<TreeNode> treeNodeList = Lists.newArrayList();
        if (!roleResourceList.isEmpty()) {
            roleResourceList.forEach(roleResource -> {
                TreeNode treeNode = new TreeNode();
                treeNode.setId(roleResource.getResourceId().toString());
                treeNode.setLabel(roleResource.getResource().getName());
                treeNodeList.add(treeNode);
            });
        }
        role.setResourceNodeList(treeNodeList);
        return CommonResult.success(role);
    }

    /**
     * 保存角色
     */
    @ApiOperation(value = "保存角色", notes = "保存角色")
    @PostMapping("/save")
    @RequiresPermissions("sys/role/save")
    public CommonResult save(@RequestBody SysRole role) {
        ValidatorUtil.validateEntity(role);
        role.setCreateTime(new Date());
        role.setCreateUserId(ShiroUtil.getUser().getId());
        sysRoleService.saveByVo(role);

        return CommonResult.success("");
    }

    /**
     * 修改角色
     */
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @PostMapping("/update")
    @RequiresPermissions("sys/role/update")
    public CommonResult update(@RequestBody SysRole role) {
        ValidatorUtil.validateEntity(role);
        role.setUpdateTime(new Date());
        role.setCreateUserId(ShiroUtil.getUser().getId());
        sysRoleService.updateByVo(role);

        return CommonResult.success("");
    }

    /**
     * 删除角色
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "roleIds", allowMultiple = true, dataType = "Integer", required = true, value = "角色ID数组", paramType = "query")
    @PostMapping("/delete")
    @RequiresPermissions("sys/role/delete")
    public CommonResult delete(@RequestBody Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);
        return CommonResult.success("");
    }

}

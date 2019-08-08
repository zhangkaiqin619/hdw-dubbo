package com.hdw.enterprise.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hdw.common.base.controller.BaseController;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.SelectNode;
import com.hdw.common.utils.StringUtils;
import com.hdw.common.utils.UUIDGenerator;
import com.hdw.enterprise.entity.EnterpriseDepartment;
import com.hdw.enterprise.entity.EnterpriseJob;
import com.hdw.enterprise.service.IEnterpriseDepartmentService;
import com.hdw.enterprise.service.IEnterpriseJobService;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description com.hdw.enterprise.controller
 * @Author TuMinglong
 * @Date 2018/12/17 11:30
 */
@Api(value = "企业职务配置表接口", tags = {"企业职务配置表接口"})
@RestController
@RequestMapping("enterprise/enterpriseJob")
public class EnterpriseJobController extends BaseController {
    @Reference
    private IEnterpriseJobService enterpriseJobService;

    @Reference
    private IEnterpriseDepartmentService enterpriseDepartmentService;

    /**
     * 列表
     */
    @ApiOperation(value = "企业职务配置表列表", notes = "企业职务配置表列表")
    @GetMapping("/list")
    @RequiresPermissions("enterprise/enterpriseJob/list")
    public Object list(@RequestParam Map<String, Object> params) {
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        PageParams page = enterpriseJobService.selectDataGrid(params);
        return ResultMap.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation(value = "获取企业职务配置表", notes = "获取企业职务配置表")
    @GetMapping("/info/{id}")
    @RequiresPermissions("enterprise/enterpriseJob/info")
    public Object info(@PathVariable("id") String id) {
        EnterpriseJob enterpriseJob = enterpriseJobService.getById(id);
        EnterpriseDepartment department=enterpriseDepartmentService.getById(enterpriseJob.getDepartmentId());
        enterpriseJob.setEnterpriseDepartment(department);
        return ResultMap.ok().put("enterpriseJob", enterpriseJob);
    }

    /**
     * 保存
     */
    @ApiOperation(value = "保存企业职务配置表", notes = "保存企业职务配置表")
    @PostMapping("/save")
    @RequiresPermissions("enterprise/enterpriseJob/save")
    public Object save(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            ShiroUser shiroUser = ShiroKit.getUser();
            enterpriseJob.setId(UUIDGenerator.getUUID());
            enterpriseJob.setCreateTime(new Date());
            enterpriseJob.setCreateUser(ShiroKit.getUser().getLoginName());
            enterpriseJobService.save(enterpriseJob);
            return ResultMap.ok("添加成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改企业职务配置表", notes = "修改企业职务配置表")
    @PostMapping("/update")
    @RequiresPermissions("enterprise/enterpriseJob/update")
    public Object update(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            enterpriseJob.setUpdateUser(ShiroKit.getUser().getLoginName());
            enterpriseJob.setUpdateTime(new Date());
            enterpriseJobService.updateById(enterpriseJob);
            return ResultMap.ok("修改成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }

    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除企业职务配置表", notes = "删除企业职务配置表")
    @PostMapping("/delete")
    @RequiresPermissions("enterprise/enterpriseJob/delete")
    public Object delete(@RequestBody String[] ids) {
        try {
            enterpriseJobService.removeByIds(Arrays.asList(ids));
            return ResultMap.ok("删除成功！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 企业部门职位选择
     *
     * @param deptId
     * @return
     */
    @ApiOperation(value = "企业部门职位选择", notes = "企业部门职位选择")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "String", paramType = "query")
    @GetMapping("/selectJobTree")
    public ResultMap selectJobTree(@RequestParam String deptId) {
        try {
            List<SelectNode> nodeList = Lists.newArrayList();
            Map<String,Object> params= Maps.newHashMap();
            if(StringUtils.isNotBlank(deptId)){
                params.put("deptId",deptId);
            }
            List<EnterpriseJob> jobList = enterpriseJobService.selectEnterpriseJobList(params);
            if (!jobList.isEmpty()) {
                jobList.forEach(job -> {
                    SelectNode selectNode = new SelectNode();
                    selectNode.setValue(job.getId());
                    selectNode.setLabel(job.getJobName());
                    nodeList.add(selectNode);
                });
            }
            return ResultMap.ok().put("list", nodeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

}

package com.hdw.enterprise.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hdw.common.api.CommonResult;
import com.hdw.common.model.SelectNode;
import com.hdw.common.mybatis.base.vo.LoginUserVo;
import com.hdw.common.mybatis.base.vo.PageVo;
import com.hdw.enterprise.entity.EnterpriseDepartment;
import com.hdw.enterprise.entity.EnterpriseJob;
import com.hdw.enterprise.vo.EnterpriseJobVo;
import com.hdw.enterprise.dto.EnterpriseJobDTO;
import com.hdw.enterprise.service.IEnterpriseDepartmentService;
import com.hdw.enterprise.service.IEnterpriseJobService;
import com.hdw.shiro.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 企业职务配置接口
 * @Author TuMinglong
 * @Date 2018/12/17 11:30
 */
@Slf4j
@Api(value = "企业职务配置接口", tags = {"企业职务配置接口"})
@RestController
@RequestMapping("enterprise/enterpriseJob")
public class EnterpriseJobController {
    @Reference
    private IEnterpriseJobService enterpriseJobService;

    @Reference
    private IEnterpriseDepartmentService enterpriseDepartmentService;

    /**
     * 企业职务配置列表
     */
    @ApiOperation(value = "企业职务配置列表", notes = "企业职务配置列表")
    @GetMapping("/list")
    @RequiresPermissions("enterprise/enterpriseJob/list")
    public CommonResult<PageVo<EnterpriseJobVo>> list(EnterpriseJobDTO enterpriseJobDTO) {
        LoginUserVo loginUserVo = ShiroUtil.getUser();
        // 不是管理员
        if (loginUserVo.getUserType() != 0) {
            enterpriseJobDTO.setUserId(ShiroUtil.getUser().getId());
        }
        PageVo<EnterpriseJobVo> page = enterpriseJobService.pageList(enterpriseJobDTO);
        return CommonResult.success(page);
    }


    /**
     * 企业职务配置信息
     */
    @ApiOperation(value = "企业职务配置信息", notes = "企业职务配置信息")
    @ApiImplicitParam(paramType = "query", name = "id", value = "职位ID", dataType = "String", required = true)
    @GetMapping("/info/{id}")
    @RequiresPermissions("enterprise/enterpriseJob/info")
    public CommonResult<EnterpriseJob> info(@PathVariable("id") String id) {
        EnterpriseJob enterpriseJob = enterpriseJobService.getById(id);
        EnterpriseDepartment department = enterpriseDepartmentService.getById(enterpriseJob.getDepartmentId());
        enterpriseJob.setEnterpriseDepartment(department);
        return CommonResult.success(enterpriseJob);
    }

    /**
     * 保存企业职务配置信息
     */
    @ApiOperation(value = "保存企业职务配置信息", notes = "保存企业职务配置信息")
    @PostMapping("/save")
    @RequiresPermissions("enterprise/enterpriseJob/save")
    public CommonResult save(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            LoginUserVo loginUserVo = ShiroUtil.getUser();
            enterpriseJob.setCreateTime(new Date());
            enterpriseJob.setCreateUser(ShiroUtil.getUser().getLoginName());
            enterpriseJobService.save(enterpriseJob);
            return CommonResult.success("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }
    }

    /**
     * 修改企业职务配置信息
     */
    @ApiOperation(value = "修改企业职务配置信息", notes = "修改企业职务配置信息")
    @PostMapping("/update")
    @RequiresPermissions("enterprise/enterpriseJob/update")
    public CommonResult update(@Valid @RequestBody EnterpriseJob enterpriseJob) {
        try {
            enterpriseJob.setUpdateUser(ShiroUtil.getUser().getLoginName());
            enterpriseJob.setUpdateTime(new Date());
            enterpriseJobService.updateById(enterpriseJob);
            return CommonResult.success("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }

    }

    /**
     * 删除企业职务配置信息
     */
    @ApiOperation(value = "删除企业职务配置信息", notes = "删除企业职务配置信息")
    @ApiImplicitParam(paramType = "query", name = "ids", value = "职位ID数组", dataType = "String", required = true, allowMultiple = true)
    @PostMapping("/delete")
    @RequiresPermissions("enterprise/enterpriseJob/delete")
    public CommonResult delete(@RequestBody String[] ids) {
        try {
            enterpriseJobService.removeByIds(Arrays.asList(ids));
            return CommonResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }
    }

    /**
     * 企业部门职位选择
     *
     * @param deptId
     * @return
     */
    @ApiOperation(value = "企业部门职位选择", notes = "企业部门职位选择")
    @ApiImplicitParam(paramType = "query", name = "deptId", value = "部门ID", dataType = "String", required = true)
    @GetMapping("/selectJobTree")
    public CommonResult<List<SelectNode>> selectJobTree(@RequestParam String deptId) {
        try {
            List<SelectNode> nodeList = Lists.newArrayList();
            Map<String, Object> params = Maps.newHashMap();
            if (StringUtils.isNotBlank(deptId)) {
                params.put("deptId", deptId);
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
            return CommonResult.success(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("运行异常，请联系管理员");
        }
    }

}

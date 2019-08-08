package com.hdw.enterprise.controller;


import com.google.common.collect.Lists;
import com.hdw.common.result.PageParams;
import com.hdw.common.result.ResultMap;
import com.hdw.common.result.SelectNode;
import com.hdw.common.utils.UUIDGenerator;
import com.hdw.enterprise.entity.Enterprise;
import com.hdw.enterprise.service.IEnterpriseService;
import com.hdw.upms.controller.UpLoadController;
import com.hdw.upms.entity.SysFile;
import com.hdw.upms.service.ISysDicService;
import com.hdw.upms.service.ISysFileService;
import com.hdw.upms.shiro.ShiroKit;
import com.hdw.upms.shiro.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

/**
 * @Description 企业Controller
 * @Author TuMinglong
 * @Date 2018/12/17 11:14
 */
@Api(value = "企业接口", tags = {"企业接口"})
@RestController
@RequestMapping("/enterprise")
public class EnterpriseController extends UpLoadController {
    @Reference
    private ISysDicService sysDicService;

    @Reference
    private IEnterpriseService enterpriseService;

    @Reference
    private ISysFileService sysFileService;

    private Map<String, List<Map<String, String>>> uploadFileUrls = new HashMap<String, List<Map<String, String>>>();

    /**
     * 企业列表
     *
     * @return
     */
    @ApiOperation(value = "企业列表", notes = "企业列表")
    @GetMapping("/list")
    @RequiresPermissions("enterprise/enterprise/list")
    public Object treeGrid(@RequestParam Map<String, Object> params) {
        ShiroUser shiroUser = ShiroKit.getUser();
        // 不是管理员
        if (shiroUser.getUserType() != 0) {
            params.put("userId", ShiroKit.getUser().getId());
        }
        PageParams page = enterpriseService.selectDataGrid(params);
        return ResultMap.ok().put("page", page);
    }

    @ApiOperation(value = "企业信息", notes = "企业信息")
    @GetMapping("/info/{id}")
    @RequiresPermissions("enterprise/enterprise/info")
    public ResultMap info(@PathVariable("id") String id) {
        Enterprise enterprise = enterpriseService.getById(id);
        return ResultMap.ok().put("enterprise", enterprise);
    }

    /**
     * 添加
     *
     * @param
     * @return
     */
    @ApiOperation(value = "保存企业信息", notes = "保存企业信息")
    @PostMapping("/save")
    @RequiresPermissions("enterprise/enterprise/save")
    public Object save(@Valid @RequestBody Enterprise enterprise) {
        try {
            enterprise.setId(UUIDGenerator.getUUID());
            enterprise.setCreateTime(new Date());
            enterprise.setCreateUser(ShiroKit.getUser().getLoginName());
            boolean b = enterpriseService.save(enterprise);
            saveFile(enterprise.getId());
            if (b) {
                return ResultMap.ok("添加成功！");
            } else {
                return ResultMap.ok("添加失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("添加失败，请联系管理员");
        }

    }

    @ApiOperation(value = "修改企业信息", notes = "修改企业信息")
    @PostMapping("/update")
    @RequiresPermissions("enterprise/enterprise/update")
    public Object update(@Valid @RequestBody Enterprise enterprise) {
        try {
            enterprise.setUpdateTime(new Date());
            enterprise.setUpdateUser(ShiroKit.getUser().getLoginName());
            boolean b = enterpriseService.updateById(enterprise);
            saveFile(enterprise.getId());
            if (b) {
                return ResultMap.ok("修改成功！");
            } else {
                return ResultMap.error("修改失败！");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultMap.error("编辑失败，请联系管理员");
        }
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除企业信息", notes = "删除企业信息")
    @PostMapping("/delete")
    @RequiresPermissions("enterprise/enterprise/delete")
    public ResultMap deleteBatchIds(@RequestParam Long[] ids) {
        enterpriseService.removeByIds(Arrays.asList(ids));
        return ResultMap.ok("删除成功！");
    }

    /**
     * 企业选择
     *
     * @param areaCode
     * @param industryCode
     * @return
     */
    @ApiOperation(value = "企业选择", notes = "企业选择")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "区域ID", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "industryCode", value = "行业ID", required = false, dataType = "Integer", paramType = "query")
    })
    @GetMapping("/getEnterpriseTree")
    public ResultMap getEnterpriseTree(@RequestParam(required = false, value = "areaCode") Long areaCode,
                                       @RequestParam(required = false, value = "industryCode") Long industryCode) {
        try {
            List<SelectNode> nodeList = Lists.newArrayList();
            Map<String, Object> params = new HashMap<>();
            ShiroUser shiroUser = ShiroKit.getUser();
            // 不是管理员
            if (shiroUser.getUserType() != 0) {
                params.put("userId", ShiroKit.getUser().getId());
            }
            params.put("industryCode", industryCode);
            List<Map<String, Object>> list = enterpriseService.selectEnterpriseList(params);
            if (!list.isEmpty()) {
                list.forEach(map -> {
                    SelectNode selectNode = new SelectNode();
                    selectNode.setValue(map.get("id").toString());
                    selectNode.setLabel(map.get("enterpriseName").toString());
                    nodeList.add(selectNode);
                });
            }
            return ResultMap.ok().put("list", nodeList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMap.error("运行异常，请联系管理员");
        }
    }

    /**
     * 上传附件
     */
    @PostMapping("/uploadFile")
    public Object uploadFile(@RequestParam("file") MultipartFile[] files) {
        try {
            List<Map<String, String>> uploadFileUrl = uploads(files, "enterprise");
            String fileName = "";
            String filePath = "";
            if (!uploadFileUrl.isEmpty() && uploadFileUrl.size() > 0) {
                for (Map<String, String> map : uploadFileUrl) {
                    fileName = map.get("fileName");
                    filePath = map.get("filePath");
                }
                setUploadFile(uploadFileUrl);
                return ResultMap.ok().put("filePath", filePath);
            } else {
                return ResultMap.ok().put("filePath", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("上传失败，请联系管理员");
        }
    }

    /**
     * 列示附件
     */
    @GetMapping("/selectFile/{id}")
    public Object listFile(@PathVariable("id") String id) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tableId", "t_enterprise");
        params.put("recordId", id);
        List<SysFile> fileList = sysFileService.selectFileListByTableIdAndRecordId(params);
        return ResultMap.ok().put("fileList", fileList);
    }

    /**
     * 删除附件
     */
    @GetMapping("/deleteFileById/{id}")
    public Object deleteFileById(@PathVariable("id") String id) {
        try {
            SysFile sysFile = sysFileService.getById(id);
            if (sysFile != null) {
                sysFileService.removeById(sysFile.getId());
                deleteFileFromLocal(sysFile.getAttachmentPath());
            }
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("删除失败，请联系管理员");
        }
    }

    /**
     * 删除附件(刚上传到后端的附件)
     */
    @GetMapping("/deleteFileByName")
    public Object deleteFileByName(@RequestParam String fileName) {
        try {
            List<Map<String, String>> list = getUploadFile();
            if (StringUtils.isNotBlank(fileName) && list != null && !list.isEmpty()) {
                for (Map<String, String> uploadFileUrl : list) {
                    boolean canDel = false;
                    if (uploadFileUrl.get("fileName").equalsIgnoreCase(fileName)) {
                        deleteFileFromLocal(uploadFileUrl.get("filePath"));
                        canDel = true;
                        break;
                    }
                    if (canDel) {
                        list.remove(uploadFileUrl);
                        break;
                    }
                }
            }
            return ResultMap.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("删除失败,请联系管理员");
        }
    }

    public Object saveFile(String id) {
        try {
            if (getUploadFile() != null) {
                ShiroUser user = ShiroKit.getUser();
                //获取企业ID前缀，生成UUID
                String prefix = "HDWX";
                for (Map<String, String> uploadFileUrl : getUploadFile()) {
                    String fileName = uploadFileUrl.get("fileName");
                    String filePah = uploadFileUrl.get("filePath");
                    SysFile sysFile = new SysFile();
                    sysFile.setId(UUIDGenerator.getUUID());
                    sysFile.setRecordId(id);
                    sysFile.setTableId("t_enterprise");
                    sysFile.setAttachmentGroup("企业");
                    sysFile.setAttachmentName(fileName);
                    sysFile.setAttachmentPath(filePah);
                    //附件类型(0-word,1-excel,2-pdf,3-jpg,png,4-其他等)
                    String fileType = fileName.substring(fileName.indexOf("."));
                    if ("doc".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(0);
                    } else if ("xlsx".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(1);
                    } else if ("pdf".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(2);
                    } else if ("jpg".equals(fileType.toLowerCase()) || "png".equals(fileType.toLowerCase())) {
                        sysFile.setAttachmentType(3);
                    } else {
                        sysFile.setAttachmentType(4);
                    }
                    sysFile.setSaveType(0);
                    sysFile.setIsSync(0);
                    sysFile.setCreateTime(new Date());
                    sysFile.setCreateUser(ShiroKit.getUser().getLoginName());
                    sysFileService.save(sysFile);
                }
                resetUploadFile();
            }
            return ResultMap.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ResultMap.error("保存失败，请联系管理员");
        }
    }

    private void setUploadFile(List<Map<String, String>> uploadFileUrl) {
        ShiroUser user = ShiroKit.getUser();
        Object o = uploadFileUrls.get(user.getId().toString());
        if (o == null) {
            uploadFileUrls.put(user.getId().toString(), new ArrayList<>());
        }
        uploadFileUrls.get(user.getId().toString()).addAll(uploadFileUrl);
    }

    private List<Map<String, String>> getUploadFile() {
        ShiroUser user = ShiroKit.getUser();
        return uploadFileUrls.get(user.getId().toString());
    }

    private void resetUploadFile() {
        ShiroUser user = ShiroKit.getUser();
        uploadFileUrls.remove(user.getId().toString());
    }
}

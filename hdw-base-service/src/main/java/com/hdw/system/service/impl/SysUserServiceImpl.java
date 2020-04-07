package com.hdw.system.service.impl;


import com.hdw.common.mybatis.base.vo.LoginUserVo;
import com.hdw.common.mybatis.base.service.impl.BaseServiceImpl;
import com.hdw.common.constant.CommonConstant;
import com.hdw.common.exception.GlobalException;
import com.hdw.system.entity.SysResource;
import com.hdw.system.entity.SysUser;
import com.hdw.system.mapper.SysUserMapper;
import com.hdw.system.service.ISysResourceService;
import com.hdw.system.service.ISysUserEnterpriseService;
import com.hdw.system.service.ISysUserRoleService;
import com.hdw.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
@Slf4j
@Service(interfaceName = "ISysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private ISysUserEnterpriseService sysUserEnterpriseService;
    @Autowired
    private ISysResourceService sysResourceService;

    @Override
    public SysUser selectByLoginName(String loginName) {
        SysUser sysUser = this.baseMapper.selectByLoginName(loginName);
        if (ObjectUtils.isEmpty(sysUser)) {
            return null;
        }
        return sysUser;
    }

    @Override
    public LoginUserVo selectLoginUserVoByLoginName(String loginName) {
        SysUser sysUser = this.baseMapper.selectByLoginName(loginName);
        if (ObjectUtils.isEmpty(sysUser)) {
            return null;
        }
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUser, loginUserVo);
        List<String> enterpriseIds = sysUserEnterpriseService.selectEnterpriseIdByUserId(sysUser.getId());
        loginUserVo.setEnterpriseIdList(enterpriseIds);
        return loginUserVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveByVo(SysUser user) {
        /**
         * 对于CPU密集型任务，最大线程数是CPU线程数+1。对于IO密集型任务，尽量多配点，可以是CPU线程数*2，或者CPU线程数/(1-阻塞系数)。
         * maxPoolSize=new Double(Math.floor(Runtime.getRuntime().availableProcessors()/(1-0.9))).intValue()
         */
        final ExecutorService threadPool=new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() ,
                new Double(Math.floor(Runtime.getRuntime().availableProcessors() / (1 - 0.9))).intValue(),
                1l,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );

        this.baseMapper.insert(user);
        //检查角色是否越权
        checkRole(user);

        try {
            threadPool.submit(()->{
                //保存用户与角色关系
                sysUserRoleService.saveOrUpdateUserRole(user.getId(), user.getRoleIdList());
            });
            threadPool.submit(()->{
                //保存用户与企业关系关系
                sysUserEnterpriseService.saveOrUpdateUserEnterprise(user.getId(), user.getEnterpriseIdList());
            });
        }finally {
            threadPool.shutdown();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByVo(SysUser user) {
        /**
         * 对于CPU密集型任务，最大线程数是CPU线程数+1。对于IO密集型任务，尽量多配点，可以是CPU线程数*2，或者CPU线程数/(1-阻塞系数)。
         * maxPoolSize=new Double(Math.floor(Runtime.getRuntime().availableProcessors()/(1-0.9))).intValue()
         */
        final ExecutorService threadPool=new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() ,
                new Double(Math.floor(Runtime.getRuntime().availableProcessors() / (1 - 0.9))).intValue(),
                1l,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(Runtime.getRuntime().availableProcessors()),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy()
        );

        this.updateById(user);
        //检查角色是否越权
        checkRole(user);

        try {
            threadPool.submit(()->{
                //保存用户与角色关系
                sysUserRoleService.saveOrUpdateUserRole(user.getId(), user.getRoleIdList());
            });
            threadPool.submit(()->{
                //保存用户与企业关系关系
                sysUserEnterpriseService.saveOrUpdateUserEnterprise(user.getId(), user.getEnterpriseIdList());
            });
        }finally {
            threadPool.shutdown();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatch(Long[] userIds) {
        this.removeByIds(Arrays.asList(userIds));
        //删除用户与角色关系
        sysUserRoleService.deleteBatchByUserIds(userIds);
        //删除监管用户与企业关系
        sysUserEnterpriseService.deleteBatchByUserIds(userIds);
    }

    @Override
    public Set<String> selectUserPermissions(long userId) {
        List<String> permsList;
        //系统管理员，拥有最高权限
        if (userId == CommonConstant.SUPER_ADMIN) {
            List<SysResource> menuList = sysResourceService.list();
            permsList = new ArrayList<>(menuList.size());
            for (SysResource menu : menuList) {
                permsList.add(menu.getUrl());
            }
        } else {
            permsList = this.baseMapper.selectPerms(userId);
        }
        return permsList.stream().collect(Collectors.toSet());
    }

    @Override
    public Set<String> selectUserRoles(long userId) {
        List<String> roleList = this.baseMapper.selectRoles(userId);
        return roleList.stream().collect(Collectors.toSet());
    }

    @Override
    public List<Long> selectResourceIdListByUserId(Long userId) {
        return this.baseMapper.selectResourceIdListByUserId(userId);
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUser user) {
        if (user.getRoleIdList() == null || user.getRoleIdList().size() == 0) {
            return;
        }
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        if (user.getCreateUserId() == CommonConstant.SUPER_ADMIN) {
            return;
        }
        //查询用户创建的角色列表
        List<Long> roleIdList = sysUserRoleService.selectRoleIdListByUserId(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new GlobalException("新增用户所选角色，不是本人创建");
        }
    }
}

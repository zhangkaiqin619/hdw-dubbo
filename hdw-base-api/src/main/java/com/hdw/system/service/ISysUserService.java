package com.hdw.system.service;

import com.hdw.common.mybatis.base.vo.LoginUserVo;
import com.hdw.common.mybatis.base.service.IBaseService;
import com.hdw.system.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 用户表
 *
 * @author TuMinglong
 * @date 2018-12-11 11:35:15
 */
public interface ISysUserService extends IBaseService<SysUser> {

    /**
     * 根据用户登录名查询用户信息
     *
     * @param loginName
     * @return
     */
    SysUser selectByLoginName(String loginName);

    /**
     * 根据用户登录名查询登录用户信息
     *
     * @param loginName
     * @return
     */
    LoginUserVo selectLoginUserVoByLoginName(String loginName);

    void saveByVo(SysUser user);

    void updateByVo(SysUser user);

    /**
     * 根据用户批量删除
     *
     * @param userIds
     */
    void deleteBatch(Long[] userIds);

    /**
     * 获取用户权限列表
     */
    Set<String> selectUserPermissions(long userId);

    /**
     * 获取用户角色名称
     */
    Set<String> selectUserRoles(long userId);

    /**
     * 查询用户的所有菜单ID
     *
     * @param userId
     * @return
     */
    List<Long> selectResourceIdListByUserId(Long userId);
}


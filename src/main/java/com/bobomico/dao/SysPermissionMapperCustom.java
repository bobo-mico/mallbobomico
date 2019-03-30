package com.bobomico.dao;

import com.bobomico.dao.po.SysPermission;

import java.util.List;

/**
 * @ClassName: com.bobomico.dao.mallbobomico
 * @Author: DELL
 * @Date: 2019/3/29  4:01
 * @Description: 自定义权限查询DAO
 * @version:
 */
public interface SysPermissionMapperCustom {

	// 根据用户id查询菜单
	List<SysPermission> findMenuListByUserId(Integer userId) throws Exception;

	// 根据用户id查询权限url
	List<SysPermission> findPermissionListByUserId(Integer userId) throws Exception;

}

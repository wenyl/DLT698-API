package cn.com.wenyl.bs.system.sysResource.service.impl;

import cn.com.wenyl.bs.system.auth.vo.MenuVo;
import cn.com.wenyl.bs.system.auth.vo.RouterVo;
import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import cn.com.wenyl.bs.system.sysResource.mapper.SysResourceMapper;
import cn.com.wenyl.bs.system.sysResource.service.ISysResourceService;
import cn.com.wenyl.bs.system.sysResource.vo.SysMenuTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceSelectTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceTableTreeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "sysResourceService")
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Override
    public List<SysResource> getUserBtn(String userId) {
        return getBaseMapper().getUserBtn(userId);
    }

    @Override
    public List<RouterVo> getUserRouter(String userId){
        List<SysResource> sysResourceList = getBaseMapper().getUserMenu(userId);
        return sysResourceList.stream()
                .filter(sysResource -> sysResource.getParentId() == null)
                .map(sysResource -> {
                    RouterVo routerVo = new RouterVo();
                    routerVo.setName(sysResource.getResourceName());
                    routerVo.setPath(sysResource.getMenuUrl());
                    routerVo.setComponent(sysResource.getMenuComponent());
                    routerVo.setRedirect(sysResource.getRedirect());
                    routerVo.setSortNo(sysResource.getSortNo());
                    routerVo.setChildren(getSubRouter(sysResource.getId(),sysResourceList));
                    return routerVo;
                })
                .sorted(Comparator.comparingDouble(RouterVo::getSortNo))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeTreeDataById(String id) {
        Stack<SysResource> sysResourcesStack = new Stack<>();
        SysResource rootResource = getById(id);
        // 推到栈底
        sysResourcesStack.push(rootResource);

        // 从栈顶取出节点
        SysResource currentNode = sysResourcesStack.pop();
        while(currentNode != null){
            // 获取删除节点的子节点
            LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysResource::getParentId,currentNode.getId());
            queryWrapper.eq(SysResource::getDelFlag,0);
            List<SysResource> list = list(queryWrapper);
            for(SysResource sysResource : list){
                sysResourcesStack.push(sysResource);
            }
            // 删除这个节点
            currentNode.setDelFlag(1);
            updateById(currentNode);
            // 判断这个节点是否还有兄弟节点，没有的话要把删除节点的父节点的isLeaf属性改了
            LambdaQueryWrapper<SysResource> parentQueryWrapper =new LambdaQueryWrapper<>();
            parentQueryWrapper.eq(SysResource::getParentId,currentNode.getParentId());
            parentQueryWrapper.eq(SysResource::getDelFlag,0);
            int count = count(parentQueryWrapper);
            if(count == 0){
                LambdaUpdateWrapper<SysResource> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(SysResource::getIsLeaf,Short.valueOf("1"));
                updateWrapper.eq(SysResource::getId,currentNode.getParentId());
                update(updateWrapper);
            }
            currentNode = sysResourcesStack.isEmpty()?null:sysResourcesStack.pop();
        }
    }

    @Override
    public List<SysMenuTreeVo> sysMenuTree() {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResource::getDelFlag,0);
        queryWrapper.eq(SysResource::getResourceType,0);
        List<SysResource> list = this.list(queryWrapper);
        return list.stream()
                .filter(sysResource -> sysResource.getParentId() == null)
                .map(sysResource -> {
                    SysMenuTreeVo vo = new SysMenuTreeVo();
                    vo.setSortNo(sysResource.getSortNo());
                    vo.setId(sysResource.getId());
                    vo.setLabel(sysResource.getResourceName());
                    vo.setIsLeaf(sysResource.getIsLeaf()==1);
                    vo.setValue(sysResource.getId());
                    vo.setChildren(getSubMenuTree(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysMenuTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysResourceTableTreeVo> getSysResourceTableTree() {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResource::getDelFlag,0);
        List<SysResource> list = list(queryWrapper);
        return list.stream()
                .filter(sysResource -> sysResource.getParentId() == null)
                .map(sysResource -> {
                    SysResourceTableTreeVo vo = new SysResourceTableTreeVo();
                    BeanUtils.copyProperties(sysResource,vo);
                    vo.setChildren(getSubSysResource(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysResourceTableTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysResourceSelectTreeVo> getSysResourceSelectTree() {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysResource::getDelFlag,0);
        List<SysResource> list = list(queryWrapper);
        return list.stream()
                .filter(sysResource -> sysResource.getParentId() == null)
                .map(sysResource -> {
                    SysResourceSelectTreeVo vo = new SysResourceSelectTreeVo();
                    vo.setSortNo(sysResource.getSortNo());
                    vo.setId(sysResource.getId());
                    vo.setLabel(sysResource.getResourceName());
                    vo.setIsLeaf(sysResource.getIsLeaf()==1);
                    vo.setValue(sysResource.getId());
                    vo.setChildren(getSubSysResourceSelectTree(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysResourceSelectTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuVo> getUserMenu(String userId) {
        List<SysResource> sysResourceList = getBaseMapper().getUserMenu(userId);
        return sysResourceList.stream()
                .filter(sysResource -> sysResource.getParentId() == null)
                .map(sysResource -> {
                    MenuVo menuVo = new MenuVo();
                    // 处理没有子菜单的一级菜单
                    if(sysResource.getParentId() == null && sysResource.getIsRoute()==0 && StringUtils.isNotEmpty(sysResource.getRedirect())){
                        menuVo.setPath(sysResource.getRedirect());
                        menuVo.setName(sysResource.getResourceName());
                        menuVo.setSortNo(sysResource.getSortNo());
                        menuVo.setChildren(new ArrayList<>());
                        menuVo.createMeta(sysResource.getMenuIcon(),sysResource.getResourceName());
                    }
                    // 处理有子菜单的一级菜单
                    if(sysResource.getParentId() == null && sysResource.getIsRoute()==0 && StringUtils.isEmpty(sysResource.getRedirect())){
                        menuVo.setPath(sysResource.getMenuUrl());
                        menuVo.setName(sysResource.getResourceName());
                        menuVo.setSortNo(sysResource.getSortNo());
                        menuVo.createMeta(sysResource.getMenuIcon(),sysResource.getResourceName());
                        menuVo.setChildren(getSubUserMenu(sysResource.getId(),sysResourceList));
                    }
                    return menuVo;
                })
                .sorted(Comparator.comparingDouble(MenuVo::getSortNo))
                .collect(Collectors.toList());
    }

    private List<MenuVo> getSubUserMenu(String parentId, List<SysResource> sysResourceList) {
        return sysResourceList.stream()
                .filter(sysResource -> StringUtils.isNotEmpty(sysResource.getParentId()) && sysResource.getParentId().equals(parentId))
                .map(sysResource -> {
                    // 处理有子菜单的一级菜单
                    MenuVo menuVo = new MenuVo();
                    menuVo.setPath(sysResource.getMenuPath());
                    menuVo.setName(sysResource.getResourceName());
                    menuVo.setSortNo(sysResource.getSortNo());
                    menuVo.createMeta(sysResource.getMenuIcon(),sysResource.getResourceName());
                    menuVo.setChildren(getSubUserMenu(sysResource.getId(),sysResourceList));
                    menuVo.setChildren(getSubUserMenu(sysResource.getId(),sysResourceList));
                    return menuVo;
                })
                .sorted(Comparator.comparingDouble(MenuVo::getSortNo))
                .collect(Collectors.toList());
    }

    private List<SysResourceSelectTreeVo> getSubSysResourceSelectTree(String id, List<SysResource> list) {
        return list.stream()
                .filter(sysResource -> StringUtils.isNotEmpty(sysResource.getParentId()) && sysResource.getParentId().equals(id))
                .map(sysResource -> {
                    SysResourceSelectTreeVo vo = new SysResourceSelectTreeVo();
                    vo.setSortNo(sysResource.getSortNo());
                    vo.setId(sysResource.getId());
                    vo.setLabel(sysResource.getResourceName());
                    vo.setIsLeaf(sysResource.getIsLeaf()==1);
                    vo.setValue(sysResource.getId());
                    vo.setChildren(getSubSysResourceSelectTree(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysResourceSelectTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    private List<SysResourceTableTreeVo> getSubSysResource(String id, List<SysResource> list) {
        return list.stream()
                .filter(sysResource -> StringUtils.isNotEmpty(sysResource.getParentId()) && sysResource.getParentId().equals(id))
                .map(sysResource -> {
                    SysResourceTableTreeVo vo = new SysResourceTableTreeVo();
                    BeanUtils.copyProperties(sysResource,vo);
                    vo.setChildren(getSubSysResource(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysResourceTableTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    private List<SysMenuTreeVo> getSubMenuTree(String id, List<SysResource> list) {
        return list.stream()
                .filter(sysResource -> StringUtils.isNotEmpty(sysResource.getParentId()) && sysResource.getParentId().equals(id))
                .map(sysResource -> {
                    SysMenuTreeVo vo = new SysMenuTreeVo();
                    vo.setSortNo(sysResource.getSortNo());
                    vo.setId(sysResource.getId());
                    vo.setLabel(sysResource.getResourceName());
                    vo.setValue(sysResource.getId());
                    vo.setIsLeaf(sysResource.getIsLeaf()==1);
                    vo.setChildren(getSubMenuTree(sysResource.getId(),list));
                    return vo;
                })
                .sorted(Comparator.comparingDouble(SysMenuTreeVo::getSortNo))
                .collect(Collectors.toList());
    }

    /**
     * 获取当前菜单的子菜单
     * @param menuId 菜单id
     * @param sysResourceList 所有菜单列表
     * @return 当前菜单的子菜单
     */
    private List<RouterVo> getSubRouter(String menuId, List<SysResource> sysResourceList) {
        return sysResourceList.stream()
                .filter(sysResource -> Objects.equals(sysResource.getParentId(),menuId))
                .map(sysResource -> {
                    RouterVo routerVo = new RouterVo();
                    routerVo.setName(sysResource.getResourceName());
                    routerVo.setPath(sysResource.getMenuUrl());
                    routerVo.setComponent(sysResource.getMenuComponent());
                    routerVo.setRedirect(sysResource.getRedirect());
                    routerVo.setSortNo(sysResource.getSortNo());
                    routerVo.setChildren(getSubRouter(sysResource.getId(),sysResourceList));
                    return routerVo;
                })
                .sorted(Comparator.comparingDouble(RouterVo::getSortNo))
                .collect(Collectors.toList());
    }
}

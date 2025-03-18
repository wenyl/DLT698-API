package cn.com.wenyl.bs.system.sysResource.controller;

import cn.com.wenyl.bs.system.sysResource.vo.SysMenuTreeVo;
import cn.com.wenyl.bs.system.sysResource.vo.SysResourceTableTreeVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.com.wenyl.bs.system.sysResource.entity.SysResource;
import cn.com.wenyl.bs.system.sysResource.service.ISysResourceService;
import cn.com.wenyl.bs.utils.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/system/sysResource")
@Api(tags="资源权限表")
public class SysResourceController{

    @Resource(name = "sysResourceService")
    private ISysResourceService sysResourceService;

    @PostMapping("/save")
    @ApiOperation(value="资源权限表-保存", notes="资源权限表-保存")
    public R<?> save(@RequestBody SysResource entity){
        //默认是叶子节点
        entity.setIsLeaf(Short.valueOf("1"));
        String parentId = entity.getParentId();
        if(StringUtils.isEmpty(parentId) && entity.getResourceType().equals(0)){
            entity.setMenuPath(entity.getMenuUrl());
        }else{
            // 更改上级节点为非叶子节点
            SysResource parentResource = sysResourceService.getById(parentId);
            parentResource.setIsLeaf(Short.valueOf("0"));
            sysResourceService.updateById(parentResource);
            if(entity.getResourceType().equals(0)){
                entity.setMenuPath(parentResource.getMenuPath()+"/"+entity.getMenuUrl());
            }
        }
        sysResourceService.save(entity);
        return R.ok();
    }

    @DeleteMapping(value = "/removeById")
    @ApiOperation(value="资源权限表-根据ID删除", notes="资源权限表-根据ID删除")
    public R<?> removeById(@RequestParam(name="id") String id) {
        sysResourceService.removeTreeDataById(id);
        return R.ok("删除成功!");
    }

    @DeleteMapping(value = "/removeByIds")
    @ApiOperation(value="资源权限表-根据ID批量删除", notes="资源权限表-根据ID批量删除")
    public R<?> removeByIds(@RequestParam(name="ids") String ids) {
        String[] idArray = ids.split(",");
        for(String id:idArray){
            sysResourceService.removeTreeDataById(id);
        }
        return R.ok("批量删除成功!");
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.PUT,RequestMethod.POST})
    @ApiOperation(value="资源权限表-根据ID更新", notes="资源权限表-根据ID更新")
    public R<?> updateById(@RequestBody SysResource entity){
        SysResource oldData = sysResourceService.getById(entity.getId());

        // 更改了上级，要查询此时上级是否还有叶子节点
        String oldParent = oldData.getParentId();
        String newParent = entity.getParentId();
        // 给孤儿找了一个爸爸，爸爸得装填改为有儿子
        if(StringUtils.isEmpty(oldParent) && StringUtils.isNotEmpty(newParent)){
            SysResource newParentResource = sysResourceService.getById(newParent);
            newParentResource.setIsLeaf(Short.valueOf("0"));
            sysResourceService.updateById(newParentResource);
            if(entity.getResourceType().equals(0)){
                entity.setMenuPath(newParentResource.getMenuPath()+"/"+entity.getMenuUrl());
            }
        }
        // 变成一个孤儿
        if(StringUtils.isNotEmpty(oldParent) && StringUtils.isEmpty(newParent)){
            LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysResource::getParentId,oldParent);
            queryWrapper.eq(SysResource::getDelFlag,0);
            int count = sysResourceService.count(queryWrapper);
            if(count == 0){
                SysResource newParentResource = sysResourceService.getById(newParent);
                newParentResource.setIsLeaf(Short.valueOf("1"));
                sysResourceService.updateById(newParentResource);
                if(entity.getResourceType().equals(0)){
                    entity.setMenuPath(newParentResource.getMenuPath()+"/"+entity.getMenuUrl());
                }
            }
        }
        // 看看是不是新找了一个爸爸
        if(StringUtils.isNotEmpty(oldParent) && StringUtils.isNotEmpty(newParent)){
            if(!oldParent.equals(newParent)){
                // 查询原父节点是否还有子节点，没有的话，将原父节点标识为叶子节点
                LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SysResource::getParentId,oldParent);
                queryWrapper.eq(SysResource::getDelFlag,0);
                int count = sysResourceService.count(queryWrapper);
                if(count == 0){
                    SysResource oldParentResource = sysResourceService.getById(oldParent);
                    oldParentResource.setIsLeaf(Short.valueOf("1"));
                    sysResourceService.updateById(oldParentResource);
                }


                // 选择了新的父节点，将新的父节点设置为非叶子节点
                SysResource newParentResource = sysResourceService.getById(oldParent);
                newParentResource.setIsLeaf(Short.valueOf("0"));
                sysResourceService.updateById(newParentResource);

                if(entity.getResourceType().equals(0)){
                    entity.setMenuPath(newParentResource.getMenuPath()+"/"+entity.getMenuUrl());
                }
            }
        }

        sysResourceService.updateById(entity);
        return R.ok();
    }

    @GetMapping(value = "/queryById")
    @ApiOperation(value="资源权限表-通过id查询", notes="资源权限表-通过id查询")
    public R<SysResource> queryById(@RequestParam(name="id") String id) {
        SysResource entity = sysResourceService.getById(id);
        if(entity==null) {
            return R.error("未找到对应数据");
        }
        return R.ok(entity);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value="资源权限表-分页查询", notes="资源权限表-分页列表查询")
    public R<IPage<SysResource>> queryPageList(
                                                       SysResource sysResource,
                                                       @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
        LambdaQueryWrapper<SysResource> queryWrapper = Wrappers.lambdaQuery(sysResource);
        Page<SysResource> page = new Page<>(currentPage, pageSize);
        IPage<SysResource> pageList = sysResourceService.page(page,queryWrapper);
        return R.ok(pageList);
    }

    @GetMapping(value = "/getSubResource")
    @ApiOperation(value="资源权限表-获取下级资源", notes="资源权限表-获取下级资源")
    public R<List<SysResource>> getSubResource(
            @RequestParam(required = false,value = "parentId") String parentId,
            HttpServletRequest req) {
        LambdaQueryWrapper<SysResource> queryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isBlank(parentId)){
            queryWrapper.isNull(SysResource::getParentId);
        }else{
            queryWrapper.eq(SysResource::getParentId,parentId);
        }

        queryWrapper.eq(SysResource::getDelFlag,0);
        List<SysResource> list = sysResourceService.list(queryWrapper);
        return R.ok(list);
    }
    @GetMapping(value = "/getMenuTree")
    @ApiOperation(value="资源权限表-获取菜单树", notes="资源权限表-获取菜单树")
    public R<List<SysMenuTreeVo>> getMenuTree(){
        List<SysMenuTreeVo> ret = sysResourceService.sysMenuTree();
        return R.ok(ret);
    }
    @GetMapping(value = "/getSysResourceTableTree")
    @ApiOperation(value="资源权限表-获取表格资源树", notes="资源权限表-获取表格资源树")
    public R<List<SysResourceTableTreeVo>> getSysResourceTableTree(){
        List<SysResourceTableTreeVo> ret = sysResourceService.getSysResourceTableTree();
        return R.ok(ret);
    }
}

package cn.com.wenyl.bs.system.sysDept.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.com.wenyl.bs.system.sysDept.entity.SysDept;
import cn.com.wenyl.bs.system.sysDept.service.ISysDeptService;
import cn.com.wenyl.bs.utils.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/system/sysDept")
@Api(tags="组织机构")
public class SysDeptController{

    @Resource(name = "sysDeptService")
    private ISysDeptService sysDeptService;

    @PostMapping("/save")
    @ApiOperation(value="组织机构-保存", notes="组织机构-保存")
    public R<?> save(@RequestBody SysDept entity){
        sysDeptService.save(entity);
        return R.ok();
    }

    @DeleteMapping(value = "/removeById")
    @ApiOperation(value="组织机构-根据ID删除", notes="组织机构-根据ID删除")
    public R<?> removeById(@RequestParam(name="id") String id) {
        sysDeptService.removeById(id);
        return R.ok("删除成功!");
    }

    @DeleteMapping(value = "/removeByIds")
    @ApiOperation(value="组织机构-根据ID批量删除", notes="组织机构-根据ID批量删除")
    public R<?> removeByIds(@RequestParam(name="ids") String ids) {
        sysDeptService.removeByIds(Arrays.asList(ids.split(",")));
        return R.ok("批量删除成功!");
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.PUT,RequestMethod.POST})
    @ApiOperation(value="组织机构-根据ID更新", notes="组织机构-根据ID更新")
    public R<?> updateById(@RequestBody SysDept entity){
        sysDeptService.updateById(entity);
        return R.ok();
    }

    @GetMapping(value = "/queryById")
    @ApiOperation(value="组织机构-通过id查询", notes="组织机构-通过id查询")
    public R<SysDept> queryById(@RequestParam(name="id") String id) {
        SysDept entity = sysDeptService.getById(id);
        if(entity==null) {
            return R.error("未找到对应数据");
        }
        return R.ok(entity);
    }

    @GetMapping(value = "/list")
    @ApiOperation(value="组织机构-分页查询", notes="组织机构-分页列表查询")
    public R<IPage<SysDept>> queryPageList(
                                                       SysDept sysDept,
                                                       @RequestParam(name="currentPage", defaultValue="1") Integer currentPage,
                                                       @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                       HttpServletRequest req) {
        LambdaQueryWrapper<SysDept> queryWrapper = Wrappers.lambdaQuery(sysDept);
        Page<SysDept> page = new Page<>(currentPage, pageSize);
        IPage<SysDept> pageList = sysDeptService.page(page,queryWrapper);
        return R.ok(pageList);
    }
}

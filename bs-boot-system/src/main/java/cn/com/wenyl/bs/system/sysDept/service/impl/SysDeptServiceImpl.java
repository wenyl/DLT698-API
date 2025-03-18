package cn.com.wenyl.bs.system.sysDept.service.impl;

import cn.com.wenyl.bs.system.sysDept.entity.SysDept;
import cn.com.wenyl.bs.system.sysDept.mapper.SysDeptMapper;
import cn.com.wenyl.bs.system.sysDept.service.ISysDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service(value = "sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

}

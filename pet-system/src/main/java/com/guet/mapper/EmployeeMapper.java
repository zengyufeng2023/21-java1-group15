package com.guet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guet.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}

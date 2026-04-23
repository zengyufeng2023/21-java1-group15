package com.guet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.entity.Employee;
import com.guet.mapper.EmployeeMapper;
import com.guet.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}

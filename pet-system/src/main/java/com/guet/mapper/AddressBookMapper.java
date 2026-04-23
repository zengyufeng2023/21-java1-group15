package com.guet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guet.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}

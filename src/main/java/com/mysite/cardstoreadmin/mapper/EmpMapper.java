package com.mysite.cardstoreadmin.mapper;

import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysite.cardstoreadmin.pojo.Emp;


public interface EmpMapper extends  BaseMapper<Emp> {
	
	Emp getEmp(@Param("empId") Integer empId);
	
	int insertEmp(Emp emp); 
}

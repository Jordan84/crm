package com.etalk.crm.dao;

import com.etalk.crm.pojo.StudentDynamics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StudentDynamicsMapper {
    /**
     * 添加学生动态信息
     * @param studentDynamics
     * @return
     */
	public int addStuDynamic(StudentDynamics studentDynamics);
}

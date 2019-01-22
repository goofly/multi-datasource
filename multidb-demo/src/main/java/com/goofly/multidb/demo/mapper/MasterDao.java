package com.goofly.multidb.demo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.goofly.multidb.demo.vo.UserInfo;

@Mapper
public interface MasterDao {
    @Select("SELECT * FROM tb_user WHERE userName = #{userName}")
    UserInfo findByName(@Param("userName") String userName);
    
    @Insert("INSERT INTO tb_user(id,userName,password) VALUES(#{id},#{userName},#{password})")
    int insertHappiness(@Param("id") Long id, @Param("userName") String userName,@Param("password") String password);
}

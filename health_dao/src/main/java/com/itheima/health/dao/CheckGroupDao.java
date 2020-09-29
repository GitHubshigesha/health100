package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/22 20:23
 */
public interface CheckGroupDao {
    //添加检查组
    void add(CheckGroup checkGroup);
    //添加检查组与检查项的关系
    //注意：两个参数类型相同，要取别名
    //使用@Param区分两个参数类型相同的参数
    void addCheckGroopCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    Page<CheckGroup> findByCondition(String queryString);
    //查询所有检查组
    List<CheckGroup> findAll();
}

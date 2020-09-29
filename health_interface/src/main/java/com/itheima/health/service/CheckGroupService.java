package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/22 20:08
 */
public interface CheckGroupService {
    //添加检查组
    void add(CheckGroup checkGroup,Integer[] checkitemIds);
    //分页查询
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);
    //查询所有检查组
    List<CheckGroup> findAll();
}

package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/18 22:46
 */
public interface CheckItemService {
    //查询所有的检查项
    List<CheckItem> findAll();
    //添加检查项
    void add(CheckItem checkItem);
    //分页条件查询
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);
    //通过id查询
    CheckItem findById(int id);
    //更新检查项
    void update(CheckItem checkItem);
    //通过id删除检查项
    void deleteById(int id)throws HealthException;
}

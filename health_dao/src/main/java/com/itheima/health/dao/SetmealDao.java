package com.itheima.health.dao;


import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/23 21:14
 */
public interface SetmealDao {
    //添加套餐
    void add(Setmeal setmeal);
    //添加套餐与检查组的关系
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);
    //条件查询
    Page findByCondition(String queryString);
    /*编辑套餐*/
    //通过id查询
    Setmeal findById(int id);
    //通过id查询选中的检查组ids
    List<Integer> findCheckgroupIdsBySetmealId(int id);
    //更新套餐信息
    void update(Setmeal setmeal);
    //删除旧关系
    void deleteSetmealCheckGroup(Integer id);
    //查询所有的套餐
    List<Setmeal> findAll();
    //查询套餐详情
    Setmeal findDetailById(int id);
    //查数据中套餐的所有图片
    List<String> findImgs();
    //通过套餐的id查询使用了这个套餐的订单个数
    int findOrderCountBySetmealId(Integer id);
    //通过id删除套餐信息
    void deleteById(Integer id);
}

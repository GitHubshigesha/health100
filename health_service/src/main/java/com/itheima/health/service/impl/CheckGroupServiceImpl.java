package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/22 20:11
 */
@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;
    //添加
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //添加检查组
        checkGroupDao.add(checkGroup);
        //获取检查组的id
        Integer checkGroupId = checkGroup.getId();
        //遍历选中检查项的id，添加检查组和检查项的关系
        if (null != checkitemIds){
            //
            for (Integer checkitemId : checkitemIds) {
                //添加检查组与检查项的关系
                checkGroupDao.addCheckGroopCheckItem(checkGroupId,checkitemId);
            }
        }
    }
    //分页条件查询
    @Override
    public PageResult<CheckGroup> findPage(QueryPageBean queryPageBean) {
        //使用pageHelper.startPage
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //有条件，则要模糊查询
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            //拼接%
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //紧接着的查询会被分页
        Page<CheckGroup>page = checkGroupDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<CheckGroup>(page.getTotal(),page.getResult());
    }
    //查询所有检查组
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}

package com.itheima.health.service.impl;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/18 22:47
 */

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Description: No Description
 * 解决 dubbo 2.6.0 【注意，注意，注意】
 * interfaceClass 发布出去服务的接口为这个CheckItemService.class
 * 没加interfaceClass, 调用No Provider 的异常
 * User: Eric
 */
@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService{
    @Autowired
    private CheckItemDao checkItemDao;
    //查询 所有检查项
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
    //添加检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
    //分页查询
    @Override
    public PageResult<CheckItem> findPage(QueryPageBean queryPageBean) {
        //第二种，Mapper接口方式的调用，推荐这种使用方式。
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 模糊查询 拼接 %
        // 判断是否有查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 有查询条件，拼接%
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 紧接着的查询语句会被分页
        Page<CheckItem> page = checkItemDao.findByCondition(queryPageBean.getQueryString());
        // 封装到分页结果对象中
        PageResult<CheckItem> pageResult = new PageResult<CheckItem>(page.getTotal(), page.getResult());
        return pageResult;
    }
    /*编辑检查项*/
    //通过id查询
    @Override
    public CheckItem findById(int id) {
        return checkItemDao.findById(id);
    }
    //查询
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }
    //通过id删除检查项
    @Override
    public void deleteById(int id) throws HealthException {
        // 先判断这个检查组是否被套餐使用了
        //调用dao查询检查项的id是否在t_checkitem表中存在记录
        int cnt =checkItemDao.findCountByCheckItemId(id);
        if (cnt > 0){
            //被使用了，则不能删除，报错
            //？？？health_web能捕获到这个异常吗？
            throw new HealthException(MessageConstant.CHECKITEM_IN_USE);
        }
        //没使用就可以调用dao删除
        checkItemDao.deleteById(id);
    }
}

package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/18 22:34
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference//导第二个包
    private CheckItemService checkItemService;
    @GetMapping("/findAll")
    public Result findAll(){
        //调用服务查询所有的检查项
        List<CheckItem> list = checkItemService.findAll();
        //封装返回的结果
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }
    /**
     *新增
     * @param checkItem 接收前端提交过来的formData
     * @return Result
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        //调用服务添加
        checkItemService.add(checkItem);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS);
    }
    //分页条件查询
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用业务来分页，获取分页结果
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        //return pageResult;
        //返回给页面，包装到Result,统一封装
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }
    //通过id查询
    @GetMapping("/findById")
    public Result findById(int id){
        CheckItem checkItem = checkItemService.findById(id);

        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem){
        //调用服务更新
        checkItemService.update(checkItem);
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
    //删除检查项
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        //调用业务服务删除
        //try{
        checkItemService.deleteById(id);
        //}catch(Exception e) {
        //  e.printStackTrace();
        //}
        //响应结果
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}

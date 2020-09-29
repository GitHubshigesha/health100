package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.util.QiNiuUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/23 20:12
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    //注入JedisPool
    @Autowired
    private JedisPool jedisPool;
    //套餐上传图片
    //【注意】此处方法的参数名必须与el-upload中的name的值要一致，大小写严格区分
    // 否则 imgFile=null
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //获取原有图片名称，截取到后缀名
        String originalFilename = imgFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成唯一文件名，拼接后缀名
        String fileName = UUID.randomUUID()+extension;
        //调用七牛云上传文件
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),fileName);
            //- 返回数据给页面
            //{   flag:
            //    message:
            //    data:{imgName: 图片名,1
            //        domain: QiNiuUtils.DOMAIN}}
            Map<String, String> map = new HashMap<>();
            map.put("imgName",fileName);
            map.put("domain",QiNiuUtils.DOMAIN);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
    }
    //添加套餐
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer [] checkgroupIds){
        //调用业务服务添加
        setmealService.add(setmeal,checkgroupIds);
        // 添加redis生成静态页面的任务
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        // redis中set集合中保存的元素格式为: 套餐id|操作类型|时间戳
        jedis.sadd("setmeal:static:html", setmeal.getId()+"|1|" + System.currentTimeMillis());
        // 还回连接池
        jedis.close();
        //响应结果
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
    //分页查询
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        //调用服务分页查询
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }
    /*编辑套餐*/
    //通过id查询套餐信息
    @GetMapping("/findById")
    public Result findById(int id){
        //调用服务查询
        Setmeal setmeal = setmealService.findById(id);
        //前端要显示的图片需要全路径
        //封装到map中，解决图片路径问题
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("setmeal",setmeal);//formData
        resultMap.put("domain",QiNiuUtils.DOMAIN);//domain
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }
    //通过id查询选中的检查组ids
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }
    //修改
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        //调用业务服务修改
        setmealService.update(setmeal,checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        jedis.sadd("setmeal:static:html",setmeal.getId()+"|1|"+ System.currentTimeMillis());
        jedis.close();
        //响应结果
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }
    //通过id删除
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        // 操作符0代表删除
        jedis.sadd("setmeal:static:html",id+"|0|"+System.currentTimeMillis());
        jedis.close();
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}

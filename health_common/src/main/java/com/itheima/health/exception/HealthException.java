package com.itheima.health.exception;

/**
 * @Author: zhangzhimin
 * @Date: 2020/9/21 17:36
 */
//自定义异常的好处：
//1. 区分业务异常与系统异常
//2. 友好提示
//3. 终止已知不符合业务逻辑代码的断续执行
public class HealthException extends RuntimeException{
    public HealthException(String message){
        super(message);
    }
}

package com.polan.netty.rpc;


import com.polan.netty.commons.entity.User;
import com.polan.netty.commons.exception.ErrorParamsException;

/**
 * <p> 测试Service</p>
 * @author youq  2019/4/19 19:50
 */
public interface DemoService {

    /**
     * 除法运算
     * @param numberA 第一个数
     * @param numberB 第二个数
     * @return 结果
     */
    double division(int numberA, int numberB) throws ErrorParamsException;

    /**
     * 获取用户信息
     * @return 用户信息
     */
    User getUserInfo();

    /**
     * 打印方法
     * @return 一个字符串
     */
    String print();

    /**
     * 求和方法
     * @param numberA 第一个数
     * @param numberB 第二个数
     * @return 两数之和
     */
    int sum(int numberA, int numberB);
}

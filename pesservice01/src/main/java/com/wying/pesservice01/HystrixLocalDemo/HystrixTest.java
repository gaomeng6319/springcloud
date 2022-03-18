package com.wying.pesservice01.HystrixLocalDemo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * description:spring cloud熔断器 运行 测试效果
 * date: 2021/12/27
 * author: gaom
 * version: 1.0
 */
public class HystrixTest {

    public static void main(String[] args){
        HystrixCommand<String> hystrixCommand=new TestHystrixCommand(HystrixCommandGroupKey.Factory.asKey("testGroup"));
        String rtn= hystrixCommand.execute();
        System.out.println(rtn);

    }

}

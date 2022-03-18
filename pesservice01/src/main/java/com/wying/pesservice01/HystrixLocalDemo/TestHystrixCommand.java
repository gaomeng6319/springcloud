package com.wying.pesservice01.HystrixLocalDemo;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * description:spring cloud熔断器  demo
 * 继承HystrixCommand类 重写 run()  getFallback() 方法
 * date: 2021/12/27
 * author: gaom
 * version: 1.0
 */
public class TestHystrixCommand extends HystrixCommand<String> {

    protected TestHystrixCommand(HystrixCommandGroupKey group) {
       super(group,4000);
     }

    @Override
    protected String run() throws Exception {
        System.out.println("运行run begen");
        Thread.sleep(5000);
        System.out.println("运行run end");
        return "执行完毕!";
    }

    @Override
    protected String getFallback() {
        //return super.getFallback();
        return "超时熔断了...";
    }




}

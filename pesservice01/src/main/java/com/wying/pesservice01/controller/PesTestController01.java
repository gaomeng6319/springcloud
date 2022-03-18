package com.wying.pesservice01.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static com.wying.pesservice01.util.PtConstant.*;

/**
 * description:测试controller
 * date: 2021/7/16
 * author: gaom
 * version: 1.0
 */
@RestController
@RequestMapping("/pesTestController01")
//启用eureka客户端
@EnableEurekaClient
//启动fegin客户端
@EnableFeignClients
public class PesTestController01 {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public PesService02FeginClient pesService02FeginClient;

    /**
     * 这个一个测试服务
     * @return
     */
    @PostMapping("/test01")
    public Map<String,Object> test01(@RequestBody HashMap<String,Object> reqMap){
        System.out.println("==============PesTestController01-->test01============");
        Map<String,Object> resMap=new HashMap<>();
        try {
            System.out.println("reqMap:"+objectMapper.writeValueAsString(reqMap));
            resMap.put($BODY,objectMapper.writeValueAsString(reqMap));
            resMap.put($CODE,$200);
            resMap.put($MESSAGE,$SUCCESS);
            System.out.println("reqMap:"+objectMapper.writeValueAsString(reqMap)+"    resMap:"+objectMapper.writeValueAsString(resMap));
            return  resMap;
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put($CODE,$301);
            resMap.put($MESSAGE,$FAIL);
            return  resMap;

        }
    }

    @GetMapping("/test02")
    public String test02(){

        return  "pesTestController01/test02 method";
    }


    /**
     *  我是pesservice01的服务 我将使用fegin调用 pesservice02 的 pesTestController02/test01 服务
     * @param reqMap
     * @return
     */
    @PostMapping("/test03")
    //配置test03方法 触发熔断后 回调test03Fallback方法
    //@HystrixCommand(fallbackMethod = "test03Fallback")
    //通过ignoreExceptions属性配置熔断器忽略的异常.
    //@HystrixCommand(fallbackMethod = "test03Fallback",ignoreExceptions={NumberFormatException.class})
    //这个方法单独配置hystrix超时时间为5S
    @HystrixCommand(fallbackMethod = "test03Fallback",ignoreExceptions={NumberFormatException.class},
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000" )
            })
 public Map<String,Object> test03(@RequestBody HashMap<String,Object> reqMap){
        System.out.println("==============PesTestController01-->test03============");

       // Integer.parseInt("s");
        String a=null;
        a.toString();

        Map<String,Object> resMap=new HashMap<>();
       // 使用fegin调用 pesservice02 的 pesTestController02/test01 服务
        System.out.println("==============使用fegin调用 pesservice02 的 pesTestController02/test01 服务 入参:"+reqMap.toString());
        resMap= pesService02FeginClient.test01(reqMap);
        System.out.println("==============使用fegin调用 pesservice02 的 pesTestController02/test01 服务 返回:"+resMap.toString());
        return  reqMap;
    }


    public Map<String, Object> test03Fallback(HashMap<String,Object> reqMap,Throwable t) {
         String errorMsg="PesService01 使用fegin客户端 调用PesService02 /pesTestController02/test01 服务失败了，触发熔断器  执行 PesTestController01 test03Fallback()方法";
        System.out.println(errorMsg);
        System.out.println("入参:"+reqMap.toString());
        System.out.println("触发熔断的原因:"+t.toString());
        //打印错误栈信息
        t.printStackTrace();
        Map<String, Object> resMap = new HashMap<>();

        resMap.put($CODE, $301);
        resMap.put($MESSAGE,errorMsg );
        return resMap;
    }


}

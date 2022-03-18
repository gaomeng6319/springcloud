package com.wying.pesservice01.controller;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * description:pesservice01项目 调用 pesservice02项目http服务 fegin客户端
 * date: 2021/7/16
 * author: gaom
 * version: 1.0
 */
//这里name写注册中心转为大写的名称
//@FeignClient("PES-SERVICE-02")


@FeignClient(value = "PES-SERVICE-02")
@Component
public interface PesService02FeginClient {

    /** pesservice02 PesTestController02 是在类上先配置了一个一级路径，方法上在配置二级路径
     * 这里代理客户端直接把一级路径也配置到方法上 可以做到通用性 方便在一个feginclient中配置所有要代理的 PesService02 的http请求
     *
     */
    //配置代理访问 pesservice02 PesTestController02 的 pesTestController02/test01服务
    @PostMapping("/pesTestController02/test01")
    public Map<String,Object> test01(@RequestBody HashMap<String,Object> reqMap);

}

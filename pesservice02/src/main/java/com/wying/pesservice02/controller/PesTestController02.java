package com.wying.pesservice02.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.wying.pesservice02.util.PtConstant.*;

/**
 * description:测试controller
 * date: 2021/7/16
 * author: gaom
 * version: 1.0
 */
@RestController
@RequestMapping("/pesTestController02")
@Api(value="PesTestController02",tags={"体检测试接口02"})
public class PesTestController02 {
    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 这个一个测试服务
     * @return
     */
    @PostMapping("/test01")
    public Map<String,Object> test01(@ApiParam(name="json数据",value="{\"xxx\":\"xx\"}",required=true) @RequestBody HashMap<String,Object> reqMap){

        System.out.println("==============PesTestController02-->test02============");
        try {
            Thread.sleep(4000);
           // Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

        return  "pesTestController02/test02 method";
    }

}

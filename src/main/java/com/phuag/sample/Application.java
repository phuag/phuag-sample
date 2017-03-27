package com.phuag.sample;

import com.phuag.sample.modules.sys.service.SysUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by vvvvvv on 2017/3/1.
 */
@SpringBootApplication
@MapperScan(basePackages = "com.phuag.sample.modules")
public class Application {

    private static final Logger log = LoggerFactory
            .getLogger(Application.class);

    @Autowired
    private SysUserService sysUserService;

//    @RequestMapping("/")
    public String sayHello(){
        log.debug("in sayHello");
        String passwordWithSalt = sysUserService.entryptPassword("asfas");
        return "Hello world!\n"+passwordWithSalt;
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Application.class,args);
    }
}

package com.example.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// 스포츠 센터
// 역할(ROLE - 인가) : user, manager ,admin

@Log4j2
@Controller //- 각 메서드가 view를 리턴한다.
public class IndexController {
    @GetMapping({"","/"})
    public String index(){
        log.info("index");
        //-> /WEB-INF/views/{{index}}.jsp
        return "index";// -> ViewResolver
    }//end of home
    @GetMapping("/user")
    public String user(){
        log.info("home");
        return "home";
    }//end of home
    @GetMapping("/manager")
    public String manager(){
        log.info("manager");
        return "manager";
    }//end of home
    @GetMapping("/admin")
    public String admin(){
        log.info("admin");
        return "admin";
    }//end of home
}

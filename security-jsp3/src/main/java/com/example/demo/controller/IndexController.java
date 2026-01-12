package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.MemberService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
// 스포츠 센터
// 역할(ROLE - 인가) : user, manager ,admin

@Log4j2
@Controller //- 각 메서드가 view를 리턴한다.
@RequiredArgsConstructor
public class IndexController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //http://localhost:8000 or http://localhost:8000/
    @GetMapping({"","/"})
    public String index(HttpServletRequest req, Model model) {
        log.info("index");
        log.info("index user호출 : " + req.isUserInRole("ROLE_USER"));
        log.info("index admin호출 :  " + req.isUserInRole("ROLE_ADMIN"));
        String role = "default";
        if(req.isUserInRole("ROLE_ADMIN")) role = "ROLE_ADMIN";
        else if(req.isUserInRole("ROLE_MANAGER")) role = "ROLE_MANAGER";
        else if(req.isUserInRole("ROLE_USER")) role = "ROLE_USER";
        //jsp화면에 상태값을 전달하기 - Model의 scope는 request 스코프 -forward
        model.addAttribute("role", role);
        return "redirect:/index.jsp";// -> ViewResolver
    }//end of home
    //http://localhost:8000/user
    //@RestController =  @Controller + @ResponseBody
    //@Controller를 사용하면 리턴값 String으로 화면이름을 찾음.
    //그런데 @ResponseBody를 두면 평문으로 출력됨.
    @RolesAllowed("ROLE_USER")
    @GetMapping("/user")
    public String user(){
        log.info("user");
        return "redirect:/userPage.jsp";
    }//end of home
    //http://localhost:8000/manager
    @RolesAllowed("ROLE_MANAGER")
    @GetMapping("/manager")
    public String manager(){
        log.info("manager");
        return "redirect:/managerPage.jsp";
    }//end of home
    //http://localhost:8000/admin
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("/admin")
    public String admin(){
        log.info("admin");
        return "redirect:/adminPage.jsp";
    }//end of home
    /*
    로그인에 대한 처리는 필터에서 전담해서 처리됨
    절대로 아래와 같은 엔드포인트 작성하지 말것.
    @GetMapping("/loginProcess")
    public @ResponseBody String loginProcess(){
        return "login처리는 시큐리티 필터가 담당함.";
    }
    */
    //로그인 화면 요청하기
    //http://localhost:8000/loginForm
    @GetMapping("/loginForm")
    public String loginForm(){
        log.info("loginForm");
        return "redirect:/auth/loginForm.jsp";
    }//end of loginForm

    //회원가입 화면 호출하기
    //http://localhost:8000/joinForm
    @GetMapping("/joinForm")
    public String joinForm(){
        log.info("joinForm");
        // auth/joinForm -> 응답페이지 화면 이름이다.
        // yaml -> /WEB-INF/views/ 접두어
        // 접미어     -> .jsp
        return "redirect:/auth/joinForm.jsp";//ViewResolver가 관여하지 않음
    }
    //접근권한(403번에러)이 없는 경우 처리하기
    @GetMapping("/access-denied")
    public String accessDenied(){
        log.info("accessDenied");
        //return "auth/accessDenied";//WEB-INF/views/ 아래서 찾음
        return "redirect:/accessDenied.jsp";
    }
    //http://localhost:8000/login-error
    @GetMapping("/login-error")
    public @ResponseBody String loginError(){
        log.info("login-error");
        return "아이디나 비밀번호가 맞지 않습니다.";
    }//end of loginError
    //회원가입 구현하기
    @PostMapping("/join")
    public String join(User user){
        log.info("join");
        user.setRole("ROLE_MANAGER");
        // 패스워드 암호화 하기
        String rawPassword = user.getPassword();//평문암호- 처리안됨
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);//암호화 알고리즘
        //비번 123으로 등록은 됨. 그러나 시큐리티 로그인 할 수 없음
        //왜냐면 암호화가 되지 않은 비번에 대해서는 처리안됨
        user.setPassword(encPassword);
        memberService.memberInsert(user);
        return "redirect:/auth/loginForm.jsp";//회원가입이 되면 이요청을 보냄
    }

}

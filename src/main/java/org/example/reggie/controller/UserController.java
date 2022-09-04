package org.example.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.example.reggie.dto.UserDto;
import org.example.reggie.entity.User;
import org.example.reggie.service.UserService;
import org.example.reggie.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
//        获取手机号
        String phone = user.getPhone();

        if (StringUtils.hasText(phone)) {
//        生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//        发送验证码
            log.info("code:{}", code);
//        将生成的验证码保存到Session
            session.setAttribute("code", code);
            return R.success(code);
        }

        return R.error("短信发送失败");
    }


    @PostMapping("/login")
    public R<User> login(@RequestBody UserDto userDTO, HttpSession session) {
        log.info(userDTO.toString());
//        获取手机号
        String phone = userDTO.getPhone();
//        获取验证码
        String code = userDTO.getCode();
//        从Session中获取保存的验证码
        Object codeInSession = session.getAttribute("code");
//        进行验证码的比对
        if (codeInSession != null && codeInSession.equals(code)) {
//        若比对成功，则登陆成功
//        判断当前手机号是否为新用户，若是则自动注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登陆失败");

    }

    @PostMapping("/loginout")
    public R<String> logout(HttpSession session) {
        session.removeAttribute("user");
        return R.success("账号已退出");
    }
}

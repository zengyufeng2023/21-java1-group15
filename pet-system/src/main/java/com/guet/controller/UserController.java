package com.guet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guet.common.BaseContext;
import com.guet.common.R;
import com.guet.entity.AddressBook;
import com.guet.entity.Employee;
import com.guet.entity.User;
import com.guet.service.UserService;
import com.guet.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户端登录退出
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送邮箱验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) throws MessagingException {
        //获取邮箱
        String email = user.getEmail();

        if (StringUtils.isNotEmpty(email)) {
            //随机生成一个验证码
            String code = MailUtils.achieveCode();
            log.info("code={}", code);

            //调用qq邮箱提供的服务API完成发送短信
            MailUtils.sendMail(email,code);

            //需要将生成的验证码保存到Session
            session.setAttribute(email, code);

            return R.success("邮箱验证码短信发送成功");
        }

        return R.error("信息发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());

        //获取邮箱
        String email = map.get("email").toString();

        // todo 获取验证码 你改的前端没有包含验证码
        String code = map.get("code").toString();

        //从Session中获取保存的验证码
        Object codeInSession = session.getAttribute(email);

        //进行验证码的比对（页面提交的验证码和Session中保存的验证码比对）
        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getEmail, email);

            User user = userService.getOne(queryWrapper);
            if (user == null) {
                //判断当前手机号对应的用户是否为新用户，如果是新用户就自动完成注册
                user = new User();
                user.setEmail(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }


}

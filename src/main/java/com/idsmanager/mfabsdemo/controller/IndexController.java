/*
 * Copyright (c) 2016 BeiJing JZYT Technology Co. Ltd
 * www.idsmanager.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BeiJing JZYT Technology Co. Ltd ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you
 * entered into with BeiJing JZYT Technology Co. Ltd.
 */
package com.idsmanager.mfabsdemo.controller;

import com.alibaba.fastjson.JSON;
import com.idsmanager.mfabsdemo.utils.AESUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 假设该项目启动后占用地址为 http://localhost:8080/mfa-bs-demo
 * BS 对接流程:
 * 1. 在2FA中添加BS应用, 假设应用ID填写为 test1234   ,回调地址填写为 http://localhost:8080/mfa-bs-demo/public/otp/callback，添加完成后获得js地址(https://2fa.idpsso.net:8443/2FA/public/mfa/bs/v1/javascript/test1234.jsx?state={your_state}&encryptedUsername={encryptedUsername})和aes加密key(D6zyFzz1NnlXDOEq)
 * 2. 启动该应用，浏览器打开 http://localhost:8080/mfa-bs-demo
 * 3. 输入 test/Jzyt@2018 点击登录
 * 4. 在引入BS应用之前，登录后应跳转为 http://localhost:8080/mfa-bs-demo/welcome，引入后要求跳转为一空白页面，如 http://localhost:8080/mfa-bs-demo/otp ，该页面引入上方获得的js地址，如地址为 http://192.168.1.16:8080/2FA/public/mfa/bs/v1/javascript/{应用ID}.jsx?state={state状态，该状态可放置为sessionID、时间戳、请求token等，可用于防止重放攻击}&encryptedUsername={URL编码(AES加密后的用户名)}
 * 5. 引入后在界面上会显示一个二维码和输入框，使用谷歌身份验证器扫描二维码，获得OTP码，将该OTP码输入到输入框中，点击提交，提交后的数据会由浏览器发送到2FA服务器，2FA服务器校验输入的OTP码是否正确
 * 6. 校验通过后，浏览器会重定向到回调地址，如 http://localhost:8080/mfa-bs-demo/public/otp/callback，在该地址接收GET请求参数，需要使用aes加密key解密，解密后为json字符串，其中会包含在引入js时传递的state状态以及校验是否通过的状态码和用户名
 * 7. 在开发时需要使用拦截器或过滤器判断用户是否已经登录但未进行OTP码校验
 *
 * @since 1.0.0
 */
@Controller
public class IndexController {
//    private static final String AES_KEY = "D6zyFzz1NnlXDOEq";
//
//    private static final String DEFAULT_MM = "Jzyt@2018";


    @Value("${mfa.js.uri:https://2fa.idpsso.net:4433/2FA/public/mfa/bs/v1/javascript/test1234.jsx}")
    private String mfaJsUri;

    @Value("${mfa.aes.key:D6zyFzz1NnlXDOEq}")
    private String mfaAesKey;

    @Value("${bs.demo.default.password:Jzyt@2018}")
    private String defaultPwd;


    /**
     * 引导登录
     */
    @GetMapping("/")
    public String index() {
        return "login";
    }

    /**
     * 提交登录
     */
    @PostMapping("/login")
    public String login(String username, String password, HttpSession session) {
        if (defaultPwd.equals(password)) {
            session.setAttribute("username", username);
            session.setAttribute("otp", 1);
            return "redirect:/otp";
        }
        return "redirect:/";
    }


    /**
     * 进行 多因子认证入口
     */
    @GetMapping("/otp")
    public String otp(Model model, HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        if (!StringUtils.hasLength(username)) {
            return "redirect:/";
        }
        model.addAttribute("username", URLEncoder.encode(AESUtils.aesEncrypt(username, mfaAesKey), "UTF-8"));
        model.addAttribute("mfaJsUri", mfaJsUri);
        return "otp";
    }


    /**
     * 2FA中 B/S应用 回调地址
     *
     * code值说明：
     * 1461：B/S应用未启用
     * 1462：二次认证成功
     * 1463：未验证通过
     * 1464：未验证通过的请求参数
     * 1465：用户取消了二次验证
     */
    @GetMapping("/public/otp/callback")
    public String otpCallback(String callBackParams, HttpSession session) throws Exception {
        String callBack = AESUtils.aesDecrypt(callBackParams, mfaAesKey);
        Map map = JSON.parseObject(callBack, Map.class);
        Integer code = (Integer) map.get("code");
        // 1462：二次认证成功
        if (code != null && code == 1462) {
            session.setAttribute("username", map.get("username"));
            session.setAttribute("otp", 2);
            return "redirect:/welcome";
            // 1465：用户取消了二次验证
        } else if (code != null && code == 1465) {
            session.setAttribute("otp", 0);
            return "redirect:/";
        } else {
            session.setAttribute("otp", 1);
            return "redirect:/otp";
        }
    }


    /**
     * 登录成功后的欢迎页面
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}

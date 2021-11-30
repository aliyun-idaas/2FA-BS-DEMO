# 2FA-BS-DEMO
2FA中用于B/S应用JS集成的演示和集成参考 。

开源协议：[GPL-3.0 License](https://github.com/aliyun-idaas/2FA-BS-DEMO/blob/main/LICENSE) 。


## 效果展示
![mfa-otp.gif](./relation/mfa-otp.gif)


## 开发环境要求
- Java  v1.8+
- Maven 3.3+
- 字符编码：UTF-8

## 使用框架与版本
- Spring-Boot  v2.0.2.RELEASE
- fastjson v1.2.78


## 如何使用
> 前提：需要有部署2FA的实例。
1. clone工程到本地开发环境（需要安装Maven, JDK）。
2. 在2FA中以安全管理员登录，创建一个 B/S网站的应用实例，添加时的'回调地址'填写 http://localhost:8080/public/otp/callback
3. 在创建成功的B/S网站应用 详情中，依次复制以下信息到application.properties中进行配置
- 应用加解密KEY    ->  mfa.aes.key
- JS URL                ->  mfa.js.uri  (注意，复制时要去掉 ?及之后的内容)
4. 运行 MfaBsDemoApplication.java 启动 DEMO程序，浏览器访问 http://localhost:8080/ 登录即可进行测试（账户名可随意输入，
密码使用页面上展示的）。     


## 更多帮助
若需要更多帮助，可访问部署的2FA实例的开发者文档中的'B/S网站对接Demo案例'内容。
      
package com.jtd.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Properties;

/**
 * Created by duber on 2017/3/20.
 */
@Controller
public class EmailController {

    //,String theme,String content,String name,String email)
    @RequestMapping("/xinmt/SendEmail")
    @ResponseBody
    public String SendEmail(HttpServletRequest request){

        // 创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", "smtp.exmail.qq.com");
        //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
        props.put("mail.smtp.port", "587");
        // 此处填写你的账号
        props.put("mail.user", "contact@newmedia-hk.com");
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", "Xinmt123456");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                return new PasswordAuthentication("contact@newmedia-hk.com", "Xinmt123456");
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);

        String ret_str = "发送邮件成功!";

        try{
            // 创建一个默认的 MimeMessage 对象
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(form);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress("marketing@newmedia-hk.com");
            message.setRecipient(Message.RecipientType.TO, to);

            // 设置邮件标题
            String theme = request.getParameter("theme").toString();
            message.setSubject(theme, "UTF-8");

            // 设置邮件的内容体
            String content = request.getParameter("content").toString();
            String name = request.getParameter("name").toString();
            String email = request.getParameter("email").toString();

            content +="<br/>姓名: "+name;
            content +="<br/>联系邮箱: "+email;
            message.setContent(content, "text/html;charset=UTF-8");

            HttpSession session = request.getSession();
            if(session.getAttribute(email)!=null) {
                int count = Integer.parseInt(session.getAttribute(email).toString());
                if(count<=3){
                    // 最后当然就是发送邮件啦
                    Transport.send(message);
                    count++;
                    session.setAttribute(email, count);
                }else{
                    ret_str = "发送邮件失败,您发送的邮件次数过多!";
                }
            }else {
                // 最后当然就是发送邮件啦
                Transport.send(message);
                session.setAttribute(email, 1);
            }
        }catch (MessagingException mex) {
            mex.printStackTrace();
            ret_str = "发送邮件失败,请重新发送!";
        }

       return ret_str;
    }
}

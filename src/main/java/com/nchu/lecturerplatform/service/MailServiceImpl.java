package com.nchu.lecturerplatform.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailServiceImpl implements MailService{

    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.from}")
    private String from;


    /**
     * html邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    @Override
    public void sendHtmlMail(String to, String subject, String content) {
        //获取MimeMessage对象
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true);
            //邮件发送人
            messageHelper.setFrom(from);
            //邮件接收人
            messageHelper.setTo(to);
            //邮件主题
            message.setSubject(subject);
            //邮件内容，html格式
            messageHelper.setText(content, true);
            //发送
            javaMailSender.send(message);
            //日志信息
            log.info("邮件已经发送...");
        } catch (MessagingException e) {
            log.error("发送邮件时发生异常！", e);
        }
    }


    /**
     * 发送简单邮件
     * @param to 接受者。邮件的接受方
     * @param subject 主题。邮箱标题
     * @param content 内容。是邮箱的Text
     */
    @Override
    public void sendMail(String to, String subject, String content) {

        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setFrom("3602990246@qq.com");//发起者
        mailMessage.setTo(to);//接受者
        //多人mailMessage.setTo("1xx.com","2xx.com","3xx.com");
        mailMessage.setSubject(subject);
        mailMessage.setText(content);
        try {
            javaMailSender.send(mailMessage);
            System.out.println("已发送简单邮件");
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("发送简单邮件失败");
        }
    }
}

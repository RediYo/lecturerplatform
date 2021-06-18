package com.nchu.lecturerplatform.service;

public interface MailService {

    /**
     * 发送HTML邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendHtmlMail(String to, String subject, String content);

    void sendMail(String to,String subject,String content);
}

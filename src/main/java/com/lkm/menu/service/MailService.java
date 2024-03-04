package com.lkm.menu.service;

import com.lkm.menu.util.DateFormatUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * Created by Linkm on 2023/1/10.
 */
public class MailService {

    //发件人的邮箱和密码（替换为自己的邮箱和密码）
    //PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static String myEmailAccount = "s87586485@163.com";
    public static String myEmailPassword = "WXAUSPFWGXYOSNXJ";

    //发件人邮箱的 SMTP服务器地址,必须准确,不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    //网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String myEmailSMTPHost = "smtp.163.com";

    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String receiveMailAccount = "linkm@yonyou.com";


    public static void send(List<String> mails, String msg) throws Exception {
        //1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    //参数配置
        props.setProperty("mail.transport.protocol", "smtp");   //使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   //发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            //需要请求认证
        for (String mail : mails) {
            //2.根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);

            //3.创建一封邮件
            MimeMessage message = createMimeMessage(session, myEmailAccount, mail, msg);

            //4.根据 Session获取邮件传输对象
            Transport transport = session.getTransport();

            // 5.使用邮箱账号和密码连接邮件服务器, 这里认证的邮箱必须与 message中的发件人邮箱一致,否则报错
            //
            //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
            //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
            //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            //           (1)邮箱没有开启 SMTP 服务;
            //           (2)邮箱密码错误, 例如某些邮箱开启了独立密码;
            //           (3)邮箱服务器要求必须要使用 SSL 安全连接;
            //           (4)请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            //           (5)如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            //PS_03:仔细看log,认真看log,看懂log,错误原因都在log已说明。
            transport.connect(myEmailAccount, myEmailPassword);
            //6.发送邮件,发到所有的收件地址,message.getAllRecipients()获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("邮件发送成功");
            // 7. 关闭连接
            transport.close();
        }
    }

    public static void sendFail(List<String> mails, String msg) throws Exception {
        //1.创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    //参数配置
        props.setProperty("mail.transport.protocol", "smtp");   //使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   //发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            //需要请求认证
        for (String mail : mails) {
            //2.根据配置创建会话对象, 用于和邮件服务器交互
            Session session = Session.getInstance(props);

            //3.创建一封邮件
            MimeMessage message = createMimeMessageFail(session, myEmailAccount, mail, msg);

            //4.根据 Session获取邮件传输对象
            Transport transport = session.getTransport();

            // 5.使用邮箱账号和密码连接邮件服务器, 这里认证的邮箱必须与 message中的发件人邮箱一致,否则报错
            //
            //    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
            //           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
            //           类型到对应邮件服务器的帮助网站上查看具体失败原因。
            //
            //    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
            //           (1)邮箱没有开启 SMTP 服务;
            //           (2)邮箱密码错误, 例如某些邮箱开启了独立密码;
            //           (3)邮箱服务器要求必须要使用 SSL 安全连接;
            //           (4)请求过于频繁或其他原因, 被邮件服务器拒绝服务;
            //           (5)如果以上几点都确定无误, 到邮件服务器网站查找帮助。
            //
            //PS_03:仔细看log,认真看log,看懂log,错误原因都在log已说明。
            transport.connect(myEmailAccount, myEmailPassword);
            //6.发送邮件,发到所有的收件地址,message.getAllRecipients()获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(message, message.getAllRecipients());
            System.out.println("邮件发送成功");
            // 7. 关闭连接
            transport.close();
        }
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail, String msg) throws Exception {
        // 1.创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2.From:发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "自动打卡提示", "UTF-8"));
        // 3.To:收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "111", "UTF-8"));
        // 4.Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss") + "打卡成功", "UTF-8");
        // 5.Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss") + "<br>打卡报文：" + msg, "text/html;charset=UTF-8");
        // 6.设置发件时间
        message.setSentDate(new Date());
        // 7.保存设置
        message.saveChanges();
        return message;
    }

    /**
     * 创建一封只包含文本的简单邮件
     *
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @param receiveMail 收件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessageFail(Session session, String sendMail, String receiveMail, String msg) throws Exception {
        // 1.创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2.From:发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, "打卡失败提示", "UTF-8"));
        // 3.To:收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "111", "UTF-8"));
        // 4.Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss") + "打卡失败", "UTF-8");
        // 5.Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(DateFormatUtil.getDateFormatStr("yyyy-MM-dd HH:mm:ss") + "<br>打卡失败，请重新抓包更换打卡报文！<br>更新报文地址：http://linkemin.gnway.cc/page/daka <br>抓取header内容：yht_access_token、a00", "text/html;charset=UTF-8");
        // 6.设置发件时间
        message.setSentDate(new Date());
        // 7.保存设置
        message.saveChanges();
        return message;
    }
}

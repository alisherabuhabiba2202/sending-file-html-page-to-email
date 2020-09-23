package ai.ecma.appsendingemailwithattachment.service;

import ai.ecma.appsendingemailwithattachment.entity.AttachmentContent;
import ai.ecma.appsendingemailwithattachment.repository.AttachmentContentRepository;
import ai.ecma.appsendingemailwithattachment.repository.AttachmentRepository;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;

    @Autowired
    private Configuration config;

    public String sendSimpleTextMail(){
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo("alisherabuhabiba@gmail.com");
        simpleMailMessage.setSubject("SpringBootApplication");
        simpleMailMessage.setText("Assalomu alaykum. Bu Spring boot applicationidan jonatilgan oddiy text mail");
        javaMailSender.send(simpleMailMessage);
        return "Success";
    }

    public String attachmentSendingMail(){
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage,true);
            helper.setTo("alisherabuhabiba@gmail.com");
            helper.setSubject("SpringBootApplication");
            helper.setText("Assalomu alaykum. Bu Spring boot applicationidan jonatilgan oddiy text mail");
            AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(UUID.fromString("1b0b5dfe-4fcd-4311-b21d-95b78ae9c62a"));
            ByteArrayDataSource attachment = new ByteArrayDataSource(attachmentContent.getContent(), "application/octet-stream");
            helper.addAttachment("From byte []",attachment);
            javaMailSender.send(mimeMessage);
            return "Success";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String sendingHTMLPage(){
        try {
            Map<String,Object> model=new HashMap<>();
            model.put("email","alisherabuhabiba@gmail.com");
            model.put("fullName","Alisher Atadjanov");
            model.put("code","Hello");
            model.put("changing","false");
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            Template template=config.getTemplate("email-template.ftl");
            String html= FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            helper.setTo("alisherabuhabiba@gmail.com");
            helper.setSubject("There is i send HTML page to email.");
            helper.setText(html,true);
            javaMailSender.send(mimeMessage);
            return "Ok";
        }catch (Exception e){
            return "Error";
        }
    }
}

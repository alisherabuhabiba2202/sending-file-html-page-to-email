package ai.ecma.appsendingemailwithattachment.controller;

import ai.ecma.appsendingemailwithattachment.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mailSender")
public class MailSenderController {

    @Autowired
    MailService mailService;

    @GetMapping("/simpleTextMail")
    public HttpEntity<?> simpleTextMail(){
        return ResponseEntity.ok(mailService.sendSimpleTextMail());
    }

    @GetMapping("/attachmentSendingMail")
    public HttpEntity<?> attachmentSendingMail(){
        return ResponseEntity.ok(mailService.attachmentSendingMail());
    }


    @GetMapping("/sendingHTMLPage")
    public HttpEntity<?> sendingHTMLPage(){
        return ResponseEntity.ok(mailService.sendingHTMLPage());
    }
}

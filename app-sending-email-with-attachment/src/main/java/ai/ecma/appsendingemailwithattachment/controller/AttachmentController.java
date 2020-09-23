package ai.ecma.appsendingemailwithattachment.controller;


import ai.ecma.appsendingemailwithattachment.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

//    @PreAuthorize("hasAnyAuthority('UPLOAD_FILE_ATTACHMENT')")
    @PostMapping("/upload")
    public HttpEntity<?> uploadFile(MultipartHttpServletRequest request) {
        return ResponseEntity.ok(attachmentService.uploadFile(request));
    }

//    @PreAuthorize("hasAnyAuthority('GET_FILE_ATTACHMENT')")
    @GetMapping("/{id}")
    public HttpEntity<?> getFile(@PathVariable UUID id) {
        return attachmentService.getFile(id);
    }

}

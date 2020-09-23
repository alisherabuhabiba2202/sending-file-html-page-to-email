package ai.ecma.appsendingemailwithattachment.service;

import ai.ecma.appsendingemailwithattachment.repository.AttachmentContentRepository;
import ai.ecma.appsendingemailwithattachment.repository.AttachmentRepository;
import ai.ecma.appsendingemailwithattachment.entity.Attachment;
import ai.ecma.appsendingemailwithattachment.entity.AttachmentContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.UUID;

@Service
public class AttachmentService {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    AttachmentContentRepository attachmentContentRepository;


    /**
     * BU RASMNI YUKLASH UCHUN
     * @param request
     * @return
     */
    public UUID uploadFile(MultipartHttpServletRequest request) {
        try {
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                MultipartFile file = request.getFile(fileNames.next());
                assert file != null;
                Attachment attachment = new Attachment(
                        file.getOriginalFilename(),
                        file.getSize(),
                        file.getContentType()
                );
                Attachment savedAttachment = attachmentRepository.save(attachment);

                AttachmentContent attachmentContent = new AttachmentContent(
                        savedAttachment,
                        file.getBytes());
                attachmentContentRepository.save(attachmentContent);
                OutputStream outputStream = new FileOutputStream("files/"+savedAttachment.getId()+".pdf");
                byte[] sourceBytes = file.getBytes();

                outputStream.write(sourceBytes, 0, sourceBytes.length);
                outputStream.close();
                return savedAttachment.getId();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BU PRICING YARATISH UCHUN
     *
     * @param id
     * @return
     */
    public HttpEntity<?> getFile(UUID id){
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("getAttachment"));
        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf(attachment.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; fileName=\"" + attachment.getName() + "\"")
                .body(attachmentContent.getContent());
    }

//    public HttpEntity<?> getPDFFile(UUID id,boolean download){
//        Attachment attachment = attachmentRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("getAttachment"));
//        AttachmentContent attachmentContent = attachmentContentRepository.findByAttachmentId(id);
//        if (download){
//            return ResponseEntity.ok()
//                    .contentType(MediaType.APPLICATION_PDF)
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\" PDFFile.pdf")
//                    .body(Base64.decodeBase64(attachmentContent.getStringContent()));
//        }else {
//            HttpHeaders header = new HttpHeaders();
//            header.setContentType(MediaType.APPLICATION_PDF);
//            return new ResponseEntity<>(Base64.decodeBase64(attachmentContent.getStringContent()), header, HttpStatus.OK);
//        }
//
//    }

}

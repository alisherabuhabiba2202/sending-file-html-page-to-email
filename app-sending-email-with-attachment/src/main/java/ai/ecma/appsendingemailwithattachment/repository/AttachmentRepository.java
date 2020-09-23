package ai.ecma.appsendingemailwithattachment.repository;

import ai.ecma.appsendingemailwithattachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {
}

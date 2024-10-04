package cz.rb.pdftool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tools")
class PdfToolController {

    private static final Logger logger = LoggerFactory.getLogger(PdfToolController.class);

    private final PdfToolService pdfToolService;
    private final DocumentTypeResolver documentTypeResolver;
    private final ResourceLoader resourceLoader;

    public PdfToolController(PdfToolService pdfToolService, DocumentTypeResolver documentTypeResolver, ResourceLoader resourceLoader) {
        this.pdfToolService = pdfToolService;
        this.documentTypeResolver = documentTypeResolver;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/{documentType}/fields")
    public Map<String, FormField> getFormFields(@PathVariable String documentType) throws IOException {
        logger.debug("Received request to get form fields for document type: {}", documentType);

        AcceptedDocumentType docType;

        try {
            docType = AcceptedDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid document type: {}", documentType);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid document type: " + documentType);
        }
        String sourcePath = documentTypeResolver.resolveSourcePath(String.valueOf(docType));
        logger.debug("Resolved source path: {}", sourcePath);

        try {
            return pdfToolService.getFormFields(sourcePath);
        } catch (IOException e) {
            logger.error("Error loading form fields from source path: {}", sourcePath, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid source path: " + sourcePath);
        }
    }

    @GetMapping("/{documentType}/document")
    public ResponseEntity<Resource> getDocument(@PathVariable String documentType) {
        logger.debug("Received request to get document for document type: {}", documentType);
        AcceptedDocumentType docType;

        try {
            docType = AcceptedDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid document type: {}", documentType);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid document type: " + documentType);
        }
        String sourcePath = documentTypeResolver.resolveSourcePath(String.valueOf(docType));
        logger.debug("Resolved source path: {}", sourcePath);

        Resource resource = resourceLoader.getResource("classpath:" + sourcePath);
        if (!resource.exists()) {
            logger.error("Resource does not exist at path: {}", sourcePath);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid source path: " + sourcePath);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}

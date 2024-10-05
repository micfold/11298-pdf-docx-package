package cz.rb.pdftool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController

// TODO: we should split these routes up to internal & external endpoints
@RequestMapping("/api/v1/tools")
class PdfToolController {

    private static final Logger logger = LoggerFactory.getLogger(PdfToolController.class);

    private final PdfToolService pdfToolService;
    private final DocumentTypeResolver documentTypeResolver;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public PdfToolController(PdfToolService pdfToolService,
                             DocumentTypeResolver documentTypeResolver,
                             ResourceLoader resourceLoader,
                             ObjectMapper objectMapper) {
        this.pdfToolService = pdfToolService;
        this.documentTypeResolver = documentTypeResolver;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{documentType}/fields")
    public Map<String, FormField> getFormFields(
            @PathVariable String documentType){
        logger.debug("Received request to get form fields for document type: {}", documentType);

        AcceptedDocumentType docType;

        try {
            docType = AcceptedDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid document type: {}", documentType);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Invalid document type: " + documentType);
        }
        String sourcePath = documentTypeResolver.resolveSourcePath(String.valueOf(docType));
        logger.debug("Resolved source path: {}", sourcePath);

        try {
            return pdfToolService.getFormFields(sourcePath);
        } catch (IOException e) {
            logger.error("Error loading form fields from source path: {}", sourcePath, e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid source path: " + sourcePath);
        }
    }

    @GetMapping("/{documentType}/source")
    public ResponseEntity<Resource> getDocument(
            @PathVariable String documentType) {
        logger.debug("Received request to get document for document type: {}", documentType);

        AcceptedDocumentType docType;

        try {
            docType = AcceptedDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid document type: {}", documentType);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Invalid document type: " + documentType);
        }
        String sourcePath = documentTypeResolver.resolveSourcePath(String.valueOf(docType));
        logger.debug("Resolved source path: {}", sourcePath);

        Resource resource = resourceLoader.getResource("classpath:" + sourcePath);
        if (!resource.exists()) {
            logger.error("Resource does not exist at path: {}", sourcePath);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid source path: " + sourcePath);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\"");
        headers.add(
                HttpHeaders.CONTENT_TYPE,
                "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/{documentType}/render")
    public ResponseEntity<byte[]> renderPdfDocument(
            @PathVariable String documentType,
            @RequestBody List<PdfFieldDTO> fieldValues) throws JsonProcessingException {
        logger.debug("Received request to fill PDF document for document type: {}", documentType);

        String jsonRequest = objectMapper.writeValueAsString(fieldValues);
        logger.info("Received request with fields: {}", jsonRequest);

        AcceptedDocumentType docType;

        try {
            docType = AcceptedDocumentType.valueOf(documentType);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Invalid document type: " + documentType);
        }

        String sourcePath = documentTypeResolver.resolveSourcePath(String.valueOf(docType));
        logger.debug("Resolved source path: {}", sourcePath);

        try {
            byte[] filledPdfDocument = pdfToolService.fillPdf(sourcePath, fieldValues);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    MediaType.APPLICATION_PDF);
            // TODO: Add partyId to filename for better identification, take it from the request header
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(documentType + "_filled.pdf")
                    .build());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(filledPdfDocument);

        } catch (IOException e) {
            logger.error("Error filling PDF document: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error filling PDF document");
        }
    }
}

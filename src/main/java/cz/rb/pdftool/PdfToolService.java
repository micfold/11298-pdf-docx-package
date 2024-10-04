package cz.rb.pdftool;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class PdfToolService {

    private static final Logger logger = LoggerFactory.getLogger(PdfToolService.class);
    private final ResourceLoader resourceLoader;

    public PdfToolService(ResourceLoader resourceLoader) {this.resourceLoader = resourceLoader;}

    public Map<String, FormField> getFormFields(String sourcePath) throws IOException {
        logger.debug("Loading resource from path: {}", sourcePath);
        Resource resource = resourceLoader.getResource("classpath:" + sourcePath);
        if (!resource.exists()) {
            logger.error("Resource does not exist at path: {}", sourcePath);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid source path: " + sourcePath);
        }

        Map<String, FormField> formFields = new HashMap<>();
        try (PDDocument document = PDDocument.load(resource.getInputStream())) {
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDAcroForm acroForm = catalog.getAcroForm();
            if (acroForm != null) {
                processFields(acroForm.getFields(), formFields);
            }
        } catch (IOException e) {
            logger.error("Error processing PDF: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return formFields;
    }

    public void processFields(List<PDField> fields, Map<String, FormField> formFields) {
        for (PDField field : fields) {
            FormField formField = new FormField();
            formField.name = field.getFullyQualifiedName();

            if (field instanceof PDNonTerminalField) {
                // Process child fields recursively
                PDNonTerminalField nonTerminalField = (PDNonTerminalField) field;
                processFields(nonTerminalField.getChildren(), formFields);
            }

            // Set the field type based on the specific field class
            if (field instanceof PDTextField) {
                formField.type = FieldType.TEXT;
            } else if (field instanceof PDCheckBox) {
                formField.type = FieldType.CHECKBOX;
            } else if (field instanceof PDSignatureField) {
                formField.type = FieldType.SIGNATURE;
            } else {
                formField.type = FieldType.OTHER;
            }

            formFields.put(formField.name, formField);
        }
    }
}

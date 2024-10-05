package cz.rb.pdftool;

import cz.rb.pdftool.mapping.PdfDocumentMapping;
import cz.rb.pdftool.model.AcceptedDocumentType;
import cz.rb.pdftool.model.FieldType;
import cz.rb.pdftool.model.FormField;
import cz.rb.pdftool.model.PdfFieldDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class PdfToolService {

    private static final Logger logger = LoggerFactory.getLogger(PdfToolService.class);
    private final ResourceLoader resourceLoader;
    private final PdfMappingFactory pdfMappingFactory;

    public PdfToolService(ResourceLoader resourceLoader,
                          PdfMappingFactory pdfMappingFactory) {
        this.resourceLoader = resourceLoader;
        this.pdfMappingFactory = pdfMappingFactory;
    }

    public Map<String, FormField> getFormFields(String sourcePath,
                                                AcceptedDocumentType documentType) throws IOException {
        logger.debug("Loading resource from path: {}", sourcePath);
        Resource resource = resourceLoader.getResource("classpath:" + sourcePath);
        if (!resource.exists()) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid source path: " + sourcePath);
        }

        Map<String, FormField> formFields = new HashMap<>();
        try (PDDocument document = PDDocument.load(resource.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm != null) {
                processFields(acroForm.getFields(), formFields, documentType);
            }
        } catch (IOException e) {
            logger.error("Error processing PDF: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return formFields;
    }

    public void processFields(List<PDField> fields,
                              Map<String, FormField> formFields,
                              AcceptedDocumentType documentType) {
        for (PDField field : fields) {
            FormField formField = new FormField();
            String originalFieldName = field.getFullyQualifiedName();

            String mappedName = getMappedName(originalFieldName, documentType);
            formField.name = mappedName != null ? mappedName : originalFieldName;

            if (field instanceof PDNonTerminalField nonTerminalField) {
                processFields(nonTerminalField.getChildren(), formFields, documentType);
            }

            if (field instanceof PDTextField) {
                formField.type = FieldType
                        .TEXT;
                formField.value = field.getValueAsString();
            } else if (field instanceof PDCheckBox checkBox) {
                formField.type = FieldType
                        .CHECKBOX;
                formField.checked = checkBox.isChecked();
            } else if (field instanceof PDSignatureField signatureField) {
                formField.type = FieldType
                        .SIGNATURE;
                formField.value = signatureField.getValueAsString();
            } else {
                formField.type = FieldType
                        .OTHER;
            }

            formFields.put(originalFieldName, formField);
        }
    }

    private String getMappedName(String fullyQualifiedName,
                                 AcceptedDocumentType documentType) {
        Class<? extends Enum<? extends PdfDocumentMapping>> mappingClass = pdfMappingFactory.getMappingForDocumentType(documentType);
        if (mappingClass == null) {
            return null;
        }

        for (Enum<? extends PdfDocumentMapping> enumConstant : mappingClass.getEnumConstants()) {
            PdfDocumentMapping mapping = (PdfDocumentMapping) enumConstant;
            if (mapping.getFormFieldName().equals(fullyQualifiedName)) {
                return mapping.getApiName();
            }
        }
        return null;
    }

    public byte[] fillPdf(String sourcePath,
                          List<PdfFieldDTO> fieldValues,
                          AcceptedDocumentType documentType) throws IOException {
        logger.debug("Filling PDF from path: {} with {} field values", sourcePath, fieldValues.size());
        Resource resource = resourceLoader.getResource("classpath:" + sourcePath);

        if (!resource.exists()) {
            logger.error("Resource does not exist at path: {}", sourcePath);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Invalid source path: " + sourcePath);
        }

        try (PDDocument document = PDDocument.load(resource.getInputStream())) {
            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                logger.error("No form found in the PDF");
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "No form found in the PDF");
            }

            Map<String, FormField> formFields = new HashMap<>();
            processFields(acroForm.getFields(), formFields, documentType);

            Map<String, PDField> apiNameToPdField = createApiNameToPdFieldMapping(acroForm.getFields(), documentType);

            for (PdfFieldDTO fieldDto : fieldValues) {
                PDField field = apiNameToPdField.get(fieldDto.getName());
                if (field != null) {
                    fillField(field, fieldDto);
                } else {
                    logger.warn("Field not found in PDF for API name: {}", fieldDto.getName());
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();

        } catch (IOException e) {
            logger.error("Error filling PDF: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error filling PDF");
        }
    }

    private Map<String, PDField> createApiNameToPdFieldMapping(List<PDField> fields,
                                                               AcceptedDocumentType documentType) {
        Map<String, PDField> apiNameToPdField = new HashMap<>();
        processFieldsForMapping(fields, apiNameToPdField, documentType);
        return apiNameToPdField;
    }

    private void processFieldsForMapping(List<PDField> fields,
                                         Map<String, PDField> apiNameToPdField,
                                         AcceptedDocumentType documentType) {
        for (PDField field : fields) {
            String originalFieldName = field.getFullyQualifiedName();
            String mappedName = getMappedName(originalFieldName, documentType);

            if (mappedName != null) {
                apiNameToPdField.put(mappedName, field);
            }

            if (field instanceof PDNonTerminalField nonTerminalField) {
                processFieldsForMapping(nonTerminalField.getChildren(), apiNameToPdField, documentType);
            }
        }
    }

    private void fillField(PDField field,
                           PdfFieldDTO fieldDto) {
        try {
            if (field instanceof PDTextField && fieldDto.getValue() != null) {
                field.setValue(fieldDto.getValue());
            } else if (field instanceof PDCheckBox && Boolean.TRUE.equals(fieldDto.getChecked())) {
                ((PDCheckBox) field).check();
            }
        } catch (IOException e) {
            logger.error("Error filling field {}: {}", fieldDto.getName(), e.getMessage());
        }
    }

}

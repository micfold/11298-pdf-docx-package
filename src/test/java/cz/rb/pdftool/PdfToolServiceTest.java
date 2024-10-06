/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool;

import cz.rb.pdftool.model.AcceptedDocumentType;
import cz.rb.pdftool.model.FieldType;
import cz.rb.pdftool.model.FormField;
import cz.rb.pdftool.model.PdfFieldDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfToolServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private PdfMappingFactory pdfMappingFactory;

    @Mock
    private Resource resource;

    @Mock
    private PDDocument pdDocument;

    @Mock
    private PDAcroForm acroForm;

    @InjectMocks
    private PdfToolService pdfToolService;

    @BeforeEach
    void setUp() throws IOException {
        when(resourceLoader.getResource(anyString())).thenReturn(resource);
        when(resource.exists()).thenReturn(true);
        when(resource.getInputStream()).thenReturn(mock(InputStream.class));
    }

    @Test
    void getFormFields_ShouldReturnFields() throws IOException {
        // Arrange
        PDTextField textField = mock(PDTextField.class);
        when(textField.getFullyQualifiedName()).thenReturn("testField");
        when(textField.getValueAsString()).thenReturn("testValue");

        List<PDField> fields = new ArrayList<>();
        fields.add(textField);

        when(pdDocument.getDocumentCatalog()).thenReturn(mock(PDDocumentCatalog.class));
        when(pdDocument.getDocumentCatalog().getAcroForm()).thenReturn(acroForm);
        when(acroForm.getFields()).thenReturn(fields);

        // Act
        Map<String, FormField> result = pdfToolService.getFormFields("test.pdf", AcceptedDocumentType.w8ben);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.containsKey("testField"));
        assertEquals(FieldType.TEXT, result.get("testField").type);
        assertEquals("testValue", result.get("testField").value);
    }

    @Test
    void getFormFields_ShouldThrowException_WhenResourceDoesNotExist() {
        // Arrange
        when(resource.exists()).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () ->
                pdfToolService.getFormFields("nonexistent.pdf", AcceptedDocumentType.w8ben));
    }

    @Test
    void fillPdf_ShouldFillFields() throws IOException {
        // Arrange
        List<PdfFieldDTO> fieldValues = List.of(
                new PdfFieldDTO("testField", "testValue", null)
        );

        PDTextField textField = mock(PDTextField.class);
        when(textField.getFullyQualifiedName()).thenReturn("testField");

        List<PDField> fields = new ArrayList<>();
        fields.add(textField);

        when(pdDocument.getDocumentCatalog()).thenReturn(mock(PDDocumentCatalog.class));
        when(pdDocument.getDocumentCatalog().getAcroForm()).thenReturn(acroForm);
        when(acroForm.getFields()).thenReturn(fields);

        // Act
        byte[] result = pdfToolService.fillPdf("test.pdf", fieldValues, AcceptedDocumentType.w8ben);

        // Assert
        assertNotNull(result);
        verify(textField).setValue("testValue");
    }
}
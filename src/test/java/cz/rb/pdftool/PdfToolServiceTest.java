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
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfToolServiceTest {

    @Mock
    private ResourceLoader resourceLoader;

    @Mock
    private PdfMappingFactory pdfMappingFactory;

    @Mock
    private Resource resource;

    @InjectMocks
    private PdfToolService pdfToolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFormFields_InvalidPath_ThrowsException() {
        // Arrange
        String sourcePath = "invalid.pdf";
        when(resourceLoader.getResource("classpath:" + sourcePath)).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> pdfToolService.getFormFields(sourcePath, AcceptedDocumentType.w8ben));
    }

    @Test
    void testProcessFields_TextField() {
        // Arrange
        PDTextField textField = mock(PDTextField.class);
        when(textField.getFullyQualifiedName()).thenReturn("TextField1");
        when(textField.getValueAsString()).thenReturn("Sample Text");

        Map<String, FormField> formFields = new HashMap<>();
        AcceptedDocumentType documentType = AcceptedDocumentType.w8ben;

        // Act
        pdfToolService.processFields(List.of(textField), formFields, documentType);

        // Assert
        assertEquals(1, formFields.size());
        FormField formField = formFields.get("TextField1");
        assertEquals(FieldType.TEXT, formField.type);
        assertEquals("Sample Text", formField.value);
    }
}

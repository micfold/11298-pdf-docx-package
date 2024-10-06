/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool;

import cz.rb.pdftool.mapping.W8benFieldMapping;
import cz.rb.pdftool.model.AcceptedDocumentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfMappingFactoryTest {

    private final PdfMappingFactory pdfMappingFactory = new PdfMappingFactory();

    @Test
    void getMappingForDocumentType_ShouldReturnCorrectMapping_ForW8ben() {
        // Act
        Class<?> mappingClass = pdfMappingFactory.getMappingForDocumentType(AcceptedDocumentType.w8ben);

        // Assert
        assertEquals(W8benFieldMapping.class, mappingClass);
    }

    @Test
    void getMappingForDocumentType_ShouldReturnNull_ForUnsupportedType() {
        // Act
        Class<?> mappingClass = pdfMappingFactory.getMappingForDocumentType(AcceptedDocumentType.w9);

        // Assert
        assertNull(mappingClass);
    }
}

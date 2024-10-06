/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTypeResolverTest {

    private DocumentTypeResolver documentTypeResolver;

    @BeforeEach
    void setUp() {
        documentTypeResolver = new DocumentTypeResolver();
        ReflectionTestUtils.setField(documentTypeResolver, "basePath", "/base/path/");
        ReflectionTestUtils.setField(documentTypeResolver, "w8benPath", "w8ben.pdf");
        ReflectionTestUtils.setField(documentTypeResolver, "w9Path", "w9.pdf");
        ReflectionTestUtils.setField(documentTypeResolver, "agreementTerminationPath", "termination.pdf");
        documentTypeResolver.init();
    }

    @Test
    void resolveSourcePath_ShouldReturnCorrectPath_ForW8ben() {
        // Act
        String path = documentTypeResolver.resolveSourcePath("w8ben");

        // Assert
        assertEquals("/base/path/w8ben.pdf", path);
    }

    @Test
    void resolveSourcePath_ShouldReturnNull_ForUnknownType() {
        // Act
        String path = documentTypeResolver.resolveSourcePath("unknown");

        // Assert
        assertNull(path);
    }
}

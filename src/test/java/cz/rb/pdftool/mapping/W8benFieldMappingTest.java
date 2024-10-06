/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool.mapping;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class W8benFieldMappingTest {

    @Test
    void fieldMapping_ShouldHaveCorrectValues() {
        // Assert
        assertEquals("topmostSubform[0].Page1[0].f_1[0]", W8benFieldMapping.FIRST_NAME.getFormFieldName());
        assertEquals("individualName", W8benFieldMapping.FIRST_NAME.getApiName());
    }

    @Test
    void allFields_ShouldHaveUniqueApiNames() {
        // Arrange
        W8benFieldMapping[] values = W8benFieldMapping.values();

        // Act & Assert
        for (int i = 0; i < values.length; i++) {
            for (int j = i + 1; j < values.length; j++) {
                assertNotEquals(
                        values[i].getApiName(),
                        values[j].getApiName(),
                        "API names should be unique"
                );
            }
        }
    }
}

/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.rb.pdftool.model.FormField;
import cz.rb.pdftool.model.PdfFieldDTO;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;


@WebMvcTest(PdfToolController.class)
class PdfToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdfToolService pdfToolService;

    @MockBean
    private DocumentTypeResolver documentTypeResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getFormFields_ShouldReturnFields() throws Exception {
        // Arrange
        Map<String, FormField> formFields = new HashMap<>();
        FormField field = new FormField();
        field.name = "testField";
        formFields.put("testField", field);

        when(pdfToolService.getFormFields(any(), any())).thenReturn(formFields);

        // Act & Assert
        mockMvc.perform(get("/api/v1/tools/w8ben/fields"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.testField.name").value("testField"));
    }

    @Test
    void renderPdfDocument_ShouldReturnPdf() throws Exception {
        // Arrange
        List<PdfFieldDTO> fieldValues = List.of(
                new PdfFieldDTO("testField", "testValue", null)
        );
        byte[] pdfBytes = new byte[] {1, 2, 3, 4};

        when(pdfToolService.fillPdf(any(), eq(fieldValues), any())).thenReturn(pdfBytes);

        // Act & Assert
        mockMvc.perform(post("/api/v1/tools/w8ben/render")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fieldValues)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"w8ben_filled.pdf\""));
    }
}
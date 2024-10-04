package cz.rb.pdftool;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@WebMvcTest(PdfToolController.class)
class PdfToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PdfToolService pdfToolService;

    @MockBean
    private DocumentTypeResolver documentTypeResolver;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testGetFormFields_ValidDocumentType() throws Exception {
        Map<String, FormField> formFields = new HashMap<>();
        when(documentTypeResolver.resolveSourcePath(anyString())).thenReturn("valid/path/to/document.pdf");
        when(pdfToolService.getFormFields(anyString())).thenReturn(formFields);

        mockMvc.perform(get("/api/v1/tools/w8ben/fields"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetFormFields_InvalidDocumentType() throws Exception {
        mockMvc.perform(get("/api/v1/tools/invalidType/fields"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetFormFields_InvalidSourcePath() throws Exception {

        when(documentTypeResolver.resolveSourcePath(anyString())).thenReturn("invalid/path/to/document.pdf");
        when(pdfToolService.getFormFields(anyString())).thenThrow(new IOException("Invalid source path"));
        mockMvc.perform(get("/api/v1/tools/w8ben/fields"))
                .andExpect(status().isInternalServerError());
    }
}
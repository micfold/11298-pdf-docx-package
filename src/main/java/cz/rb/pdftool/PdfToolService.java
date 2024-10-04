package cz.rb.pdftool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
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

        return formFields;
    }
}

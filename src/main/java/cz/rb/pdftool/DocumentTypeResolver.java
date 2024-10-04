package cz.rb.pdftool;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DocumentTypeResolver {

    @Value("${pdf.base.path}")
    private String basePath;

    @Value("${sourcePath.w8ben}")
    private String w8benPath;

    @Value("${sourcePath.w9}")
    private String w9Path;

    @Value("${sourcePath.agreementTermination}")
    private String agreementTerminationPath;

    private static final Map<String, String> documentTypeToSourcePathMap = new HashMap<>();

    @PostConstruct
    public void init() {
        documentTypeToSourcePathMap.put("w8ben", basePath + w8benPath);
        documentTypeToSourcePathMap.put("w9", basePath + w9Path);
        documentTypeToSourcePathMap.put("agreementTermination", basePath + agreementTerminationPath);
    }

    public String resolveSourcePath(String documentType) {
        return documentTypeToSourcePathMap.get(documentType);
    }
}

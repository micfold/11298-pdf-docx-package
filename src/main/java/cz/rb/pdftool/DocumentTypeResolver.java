/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

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

    @Value("${file.w8ben}")
    private String w8benPath;

    @Value("${file.w9}")
    private String w9Path;

    @Value("${file.agreementTermination}")
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

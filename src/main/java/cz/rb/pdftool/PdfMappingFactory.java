/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool;

import cz.rb.pdftool.mapping.*;
import cz.rb.pdftool.model.AcceptedDocumentType;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class PdfMappingFactory {
    private final Map<AcceptedDocumentType, Class<? extends Enum<? extends PdfDocumentMapping>>> mappings;

    public PdfMappingFactory() {
        mappings = new HashMap<>();
        mappings.put(AcceptedDocumentType.w8ben, W8benFieldMapping.class);
    }

    public Class<? extends Enum<? extends PdfDocumentMapping>> getMappingForDocumentType(AcceptedDocumentType documentType) {
        return mappings.get(documentType);
    }
}

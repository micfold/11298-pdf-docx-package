/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool.mapping;

public enum W9FieldMapping implements PdfDocumentMapping {
    // TODO: Check and map all the necessary fields
    UNKNOWN9("topmostSubform[0].Page1[0].f1_09[0]",
                "unknown9"),
    UNKNOWN11("topmostSubform[0].Page1[0].f1_11[0]",
                "unknown11"),
    UNKNOWN12("topmostSubform[0].Page1[0].f1_12[0]",
            "unknown12"),
    UNKNOWN13("topmostSubform[0].Page1[0].f1_13[0]",
            "unknown13"),
    UNKNOWN5("topmostSubform[0].Page1[0].f1_05[0]",
                "unknown5");


    private final BaseFieldMapping mapping;

    /**
     * @param formFieldName - name of the formField in the PDF
     * @param apiName      - name of the field in the API interface
     */
    W9FieldMapping(String formFieldName,
                      String apiName) {
        this.mapping = new BaseFieldMapping(formFieldName, apiName);
    }

    @Override
    public String getFormFieldName() { return mapping.getFormFieldName();}

    @Override
    public String getApiName() { return mapping.getApiName();}
}

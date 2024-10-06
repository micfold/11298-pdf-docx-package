/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool.mapping;

public class BaseFieldMapping {
    protected final String formFieldName;
    protected final String apiName;

    protected BaseFieldMapping(String formFieldName,
                               String apiName) {
        this.formFieldName = formFieldName;
        this.apiName = apiName;
    }

    public String getFormFieldName() {
        return formFieldName;
    }

    public String getApiName() {
        return apiName;
    }
}

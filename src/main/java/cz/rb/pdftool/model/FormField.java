/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormField {
    @JsonProperty("name")
    public String name;
    @JsonProperty("type")
    public FieldType type;
    @JsonProperty("value")
    public String value;
    @JsonProperty("checked")
    public Boolean checked;
}

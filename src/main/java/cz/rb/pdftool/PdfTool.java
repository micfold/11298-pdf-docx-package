package cz.rb.pdftool;

import com.fasterxml.jackson.annotation.JsonProperty;

enum AcceptedDocumentType {
    w8ben,
    w9,
    agreementTermination;
}

enum FieldType {
    TEXT,
    CHECKBOX,
    SIGNATURE,
    OTHER
}

class FormField {
    @JsonProperty("name")
    String name;
    @JsonProperty("type")
    FieldType type;
    @JsonProperty("value")
    String value;
    @JsonProperty("checked")
    Boolean checked;
}
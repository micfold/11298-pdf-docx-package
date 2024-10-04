package cz.rb.pdftool;

enum AcceptedDocumentType {
    w8ben,
    w9,
    agreementTermination
}

enum FieldType {
    TEXT,
    CHECKBOX,
    SIGNATURE,
    OTHER
}

class FormField {
    String name;
    FieldType type;
}


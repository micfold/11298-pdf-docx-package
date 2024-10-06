# PDF Tool Service

A Spring Boot service for handling PDF form operations, specifically designed for processing and managing tax-related documents like W8-BEN forms.

## Features

- Extract form fields from PDF documents
- Fill PDF forms with provided data
- Support for different document types (W8-BEN, W9, Agreement Termination)
- Field mapping between API names and PDF form field names
- Document type resolution based on configuration

## API Endpoints

### Get Form Fields

```
GET /api/v1/tools/{documentType}/fields
```

Retrieves all form fields from a specified document type.

### Get Source Document

```
GET /api/v1/tools/{documentType}/source
```

Downloads the original PDF document for the specified document type.

### Render Filled PDF

```
POST /api/v1/tools/{documentType}/render
```

Fills a PDF document with provided field values and returns the completed document.

#### Mock request body
```json
[
   {
      "name": "ttbClaimRate",
      "value": "15%",
      "checked": null
   },
   {
      "name": "ttbResidentCountry",
      "value": "Canada",
      "checked": null
   },
   {
      "name": "ttbIncomeType",
      "value": "Regular employment (Salary)",
      "checked": null
   },
   {
      "name": "dateOfBirth",
      "value": "01-01-1990",
      "checked": null
   },
   {
      "name": "checkFtinNotRequired",
      "value": "",
      "checked": false
   }
]
```

## Configuration

The service requires the following configuration properties:

```properties
pdf.base.path=
file.{documentType}=
```

## Field Mapping

The service uses a mapping system to translate between API field names and PDF form field names. This is implemented using enums that implement the `PdfDocumentMapping` interface.

Example for W8-BEN mapping:
```java
public enum W8benFieldMapping implements PdfDocumentMapping {
    FIRST_NAME(
            "topmostSubform[0].Page1[0].f_1[0]",
            "individualName"),
    COUNTRY_OF_CITIZENSHIP(
            "topmostSubform[0].Page1[0].f_2[0]",
            "citizenshipCountry")
}
```

## To add a new document type

1. Add the new type to the `AcceptedDocumentType` enum
2. Create a new enum for the document's field mappings, and add this new file to `/mapping` package
3. Add the mapping to the `PdfMappingFactory` using the `.put` method:
```java
    public PdfMappingFactory() {
            mappings = new HashMap<>();
            mappings.put(AcceptedDocumentType.w8ben, W8benFieldMapping.class);
            mappings.put(AcceptedDocumentType.newDocumen, newDocumetFieldMapping.class);
        }
```

## Dependencies

- Spring Boot
- Apache PDFBox (currently using 2.0.32 - v3.x would require code changes)
- SLF4J for logging
- Jackson for JSON processing

## Building and Running

This is a standard Spring Boot application. You can build and run it using Maven:

```bash
mvn clean install
java -jar target/pdf-tool-service.jar
```

## Error Handling

The service includes comprehensive error handling:
- Invalid document types return 404 Not Found
- Issues with PDF processing return 500 Internal Server Error
- Detailed error messages are logged for debugging

## TODO

- Split routes into internal & external endpoints
- Update signature box handling to link with authentication credentials

## License

License
Copyright (c) 2024 Blockz Inc. All rights reserved.
This software and associated documentation files (the "Software") are the
proprietary information of Blockz Inc. It is provided to you under the terms
of a license agreement with Blockz Inc and may not be copied, disclosed,
distributed, or used except in accordance with the terms of that license agreement.
/*
 * Copyright (c) 2024 Blockz Inc. All rights reserved.
 *
 * Created on 6.10.2024 by Michael Foldyna
 *
 */

package cz.rb.pdftool.model;

public class PdfFieldDTO {
    String name;
    String value;
    Boolean checked;

    public PdfFieldDTO(String name, String value, Boolean checked) {
        this.name = name;
        this.value = value;
        this.checked = checked;
    }

    public String getName() { return name;}
    public String getValue() { return value;}
    public Boolean getChecked() { return checked;}
}

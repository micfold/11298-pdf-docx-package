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

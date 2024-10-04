package cz.rb.pdftool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PdfFieldMapping {

    private static final Logger logger = LoggerFactory.getLogger(PdfFieldMapping.class);

    enum w8benFieldMapping {
        FIRST_NAME(
                "topmostSubform[0].Page1[0].f_1[0]",
                "individualName",
                "Individual Name"),
        COUNTRY_OF_CITIZENSHIP(
                "topmostSubform[0].Page1[0].f_2[0]",
                "citizenshipCountry",
                "Country of Citizenship"),
        PERMANENT_RESIDENCE_ADDRESS(
                "topmostSubform[0].Page1[0].f_3[0]",
                "permanentAddress",
                "Permanent Residence Address"),
        CITY_OR_TOWN_STATE_OR_PROVINCE(
                "topmostSubform[0].Page1[0].f_4[0]",
                "city",
                "City or Town, State or Province"),
        COUNTRY(
                "topmostSubform[0].Page1[0].f_5[0]",
                "country",
                "Country"),
        OPT_MAILING_ADDRESS(
                "topmostSubform[0].Page1[0].f_6[0]",
                "mailingAddress",
                "Mailing Address"),
        OPT_CITY_OR_TOWN_STATE_OR_PROVINCE(
                "topmostSubform[0].Page1[0].f_7[0]",
                "mailingCity",
                "Mailing City or Town, State or Province"),
        OPT_COUNTRY(
                "topmostSubform[0].Page1[0].f_8[0]",
                "mailingCountry",
                "Mailing Country"),
        US_TAXPAYER_IDENTIFICATION_NUMBER(
                "topmostSubform[0].Page1[0].f_9[0]",
                "usTaxNumber",
                "US Taxpayer Identification Number"),
        FOREIGN_TAX_IDENTIFYING_NUMBER(
                "topmostSubform[0].Page1[0].f_10[0]",
                "foreignTaxNumber",
                "Foreign Tax Identifying Number"),
        FTIN_NOT_LEGALLY_REQUIRED(
                "topmostSubform[0].Page1[0].c1_1[0]",
                "checkFtinNotRequired",
                "FTIN Not Legally Required Checkbox"),
        REFERENCE_NUMBERS(
                "topmostSubform[0].Page1[0].f_11[0]",
                "referenceNumbers",
                "Reference Numbers"),
        DATE_OF_BIRTH(
                "topmostSubform[0].Page1[0].f_12[0]",
                "dateOfBirth",
                "Date of Birth"),
        TTB_RESIDENT_OF_COUNTRY(
                "topmostSubform[0].Page1[0].f_13[0]",
                "ttbResidentCountry",
                "TTB Resident of Country"),
        TTB_ARTICLE_AND_PARAGRAPH(
                "topmostSubform[0].Page1[0].f_14[0]",
                "ttbArticleParagraph",
                "TTB Article and Paragraph"),
        TTB_CLAIM_RATE(
                "topmostSubform[0].Page1[0].f_15[0]",
                "ttbClaimRate",
                "TTB Claim Rate"),
        TTB_TYPE_OF_INCOME(
                "topmostSubform[0].Page1[0].f_16[0]",
                "ttbIncomeType",
                "TTB Type of Income"),
        TTB_EXPLANATION_FIRST_ROW(
                "topmostSubform[0].Page1[0].f_17[0]",
                "ttbExplanationFirstRow",
                "TTB Explanation First Row"),
        TTB_EXPLANATION_SECOND_ROW(
                "topmostSubform[0].Page1[0].f_18[0]",
                "ttbExplanationSecondRow",
                "TTB Explanation Second Row"),
        CAPACITY_CERTIFICATION(
                "topmostSubform[0].Page1[0].c1_2[0]",
                "checkCapacityCertification",
                "Capacity Certification Checkbox"),
        DATE_OF_SIGNATURE(
                "topmostSubform[0].Page1[0].Date[0]",
                "dateOfSignature",
                "Date of Signature"),
        NAME_OF_SIGNER(
                "topmostSubform[0].Page1[0].f_21[0]",
                "signerName",
                "Name of Signer"),

        // TODO: Will need to be changed later - link to authCredentials using GAAS
        SIGNATURE_BOX(
                "topmostSubform[0].Page1[0].f_20[0]",
                "signature",
                "Signature Box");

        w8benFieldMapping(String formFieldName, String apiName, String desc) {
            this.formFieldName = formFieldName;
            this.apiName = apiName;
            this.desc = desc;
        }

        private final String formFieldName;
        private final String apiName;
        private final String desc;

        public String getFormFieldName() {
            return formFieldName;
        }

        public String getApiName() {
            return apiName;
        }

        public String getDescription() {
            return desc;
        }

        public static w8benFieldMapping fromApiName(String apiName) {
            for (w8benFieldMapping mapping : values()) {
                if (mapping.apiName.equals(apiName)) {
                    return mapping;
                }
            }
            logger.error("No field found for API name: {}", apiName);
            return null;
        }

        public static w8benFieldMapping fromTechnicalName(String formFieldName) {
            for (w8benFieldMapping mapping : values()) {
                if (mapping.formFieldName.equals(formFieldName)) {
                    return mapping;
                }
            }
            logger.error("No field found for Form Field name: {}", formFieldName);
            return null;
        }
    }
}

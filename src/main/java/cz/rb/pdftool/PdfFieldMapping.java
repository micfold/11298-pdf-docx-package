package cz.rb.pdftool;

//public class PdfFieldMapping {
//
//    enum w8benFieldMapping {
//        FIRST_NAME(
//                "topmostSubform[0].Page1[0].f_1[0]",
//                "individualName"),
//        COUNTRY_OF_CITIZENSHIP(
//                "topmostSubform[0].Page1[0].f_2[0]",
//                "citizenshipCountry"),
//        PERMANENT_RESIDENCE_ADDRESS(
//                "topmostSubform[0].Page1[0].f_3[0]",
//                "permanentAddress"),
//        CITY_OR_TOWN_STATE_OR_PROVINCE(
//                "topmostSubform[0].Page1[0].f_4[0]",
//                "city"),
//        COUNTRY(
//                "topmostSubform[0].Page1[0].f_5[0]",
//                "country"),
//        OPT_MAILING_ADDRESS(
//                "topmostSubform[0].Page1[0].f_6[0]",
//                "mailingAddress"),
//        OPT_CITY_OR_TOWN_STATE_OR_PROVINCE(
//                "topmostSubform[0].Page1[0].f_7[0]",
//                "mailingCity"),
//        OPT_COUNTRY(
//                "topmostSubform[0].Page1[0].f_8[0]",
//                "mailingCountry"),
//        US_TAXPAYER_IDENTIFICATION_NUMBER(
//                "topmostSubform[0].Page1[0].f_9[0]",
//                "usTaxNumber"),
//        FOREIGN_TAX_IDENTIFYING_NUMBER(
//                "topmostSubform[0].Page1[0].f_10[0]",
//                "foreignTaxNumber"),
//        FTIN_NOT_LEGALLY_REQUIRED(
//                "topmostSubform[0].Page1[0].c1_01[0]",
//                "checkFtinNotRequired"),
//        REFERENCE_NUMBERS(
//                "topmostSubform[0].Page1[0].f_11[0]",
//                "referenceNumbers"),
//        DATE_OF_BIRTH(
//                "topmostSubform[0].Page1[0].f_12[0]",
//                "dateOfBirth"),
//        TTB_RESIDENT_OF_COUNTRY(
//                "topmostSubform[0].Page1[0].f_13[0]",
//                "ttbResidentCountry"),
//        TTB_ARTICLE_AND_PARAGRAPH(
//                "topmostSubform[0].Page1[0].f_14[0]",
//                "ttbArticleParagraph"),
//        TTB_CLAIM_RATE(
//                "topmostSubform[0].Page1[0].f_15[0]",
//                "ttbClaimRate"),
//        TTB_TYPE_OF_INCOME(
//                "topmostSubform[0].Page1[0].f_16[0]",
//                "ttbIncomeType"),
//        TTB_EXPLANATION_FIRST_ROW(
//                "topmostSubform[0].Page1[0].f_17[0]",
//                "ttbExplanationFirstRow"),
//        TTB_EXPLANATION_SECOND_ROW(
//                "topmostSubform[0].Page1[0].f_18[0]",
//                "ttbExplanationSecondRow"),
//        CAPACITY_CERTIFICATION(
//                "topmostSubform[0].Page1[0].c1_02[0]",
//                "checkCapacityCertification"),
//        DATE_OF_SIGNATURE(
//                "topmostSubform[0].Page1[0].Date[0]",
//                "dateOfSignature"),
//        NAME_OF_SIGNER(
//                "topmostSubform[0].Page1[0].f_21[0]",
//                "signerName"),
//
//        // TODO: Will need to be changed later - link to authCredentials using GAAS
//        SIGNATURE_BOX(
//                "topmostSubform[0].Page1[0].f_20[0]",
//                "signature");
//
//        /**
//         * @param formFieldName - name of the formField in the PDF
//         * @param apiName      - name of the field in the API interface
//         */
//        w8benFieldMapping(final String formFieldName,
//                          final String apiName) {
//            this.formFieldName = formFieldName;
//            this.apiName = apiName;
//        }
//
//        private final String formFieldName;
//        private final String apiName;
//
//        public String getFormFieldName() {
//            return formFieldName;
//        }
//
//        public String getApiName() {
//            return apiName;
//        }
//    }
//}

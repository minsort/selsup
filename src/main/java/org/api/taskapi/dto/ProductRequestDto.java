package org.api.taskapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductRequestDto {

    @JsonProperty("certificate_document")
    private String certificateDocument;

    @JsonProperty("certificate_document_date")
    private String certificateDocumentDate;

    @JsonProperty("certificate_document_number")
    private String certificateDocumentNumber;

    @JsonProperty("owner_inn")
    private String ownerInn;

    @JsonProperty("producer_inn")
    private String producerInn;

    @JsonProperty("production_date")
    private String productionDate;

    @JsonProperty("tnved_code")
    private String tnvedCode;

    @JsonProperty("uit_code")
    private String uitCode;

    @JsonProperty("uitu_code")
    private String uituCode;

}
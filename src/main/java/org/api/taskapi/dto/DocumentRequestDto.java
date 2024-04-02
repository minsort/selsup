package org.api.taskapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class DocumentRequestDto {
    @JsonProperty("description")
    private DescriptionRequestDto description;

    @JsonProperty("doc_id")
    private String docId;

    @JsonProperty("doc_status")
    private String docStatus;

    @JsonProperty("doc_type")
    private String docType;

    @JsonProperty("import_request")
    private boolean importRequest;

    @JsonProperty("owner_inn")
    private String ownerInn;

    @JsonProperty("participant_inn")
    private String participantInn;

    @JsonProperty("producer_inn")
    private String producerInn;

    @JsonProperty("production_date")
    private String productionDate;

    @JsonProperty("production_type")
    private String productionType;

    @JsonProperty("products")
    private List<ProductRequestDto> productRequestDtos;

    @JsonProperty("reg_date")
    private String regDate;

    @JsonProperty("reg_number")
    private String regNumber;

}

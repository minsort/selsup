package org.api.taskapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class DescriptionRequestDto {

    @JsonProperty("participantInn")
    private String participantInn;

}

package org.api.taskapi;

import org.api.taskapi.dto.DescriptionRequestDto;
import org.api.taskapi.dto.DocumentRequestDto;
import org.api.taskapi.dto.ProductRequestDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class TaskApiApplication {
    private static final String SIGNATURE = "My signature: '%s'";

    public static void main(String[] args) {
        SpringApplication.run(TaskApiApplication.class, args);
        CrptApid crptApid = new CrptApid(TimeUnit.MILLISECONDS, 2);

        DocumentRequestDto document = DocumentRequestDto.builder()
                .description(DescriptionRequestDto.builder().participantInn("string").build())
                .docId("doc1")
                .docStatus("status")
                .docType("LP_INTRODUCE_GOODS")
                .importRequest(true)
                .ownerInn("1234567890")
                .participantInn("1234567890")
                .producerInn("1234567890")
                .productionDate("2020-01-23")
                .productionType("type")
                .productRequestDtos(List.of(
                        ProductRequestDto.builder()
                                .certificateDocument("certificate")
                                .certificateDocumentDate("2020-01-23")
                                .certificateDocumentNumber("123")
                                .ownerInn("1234567890")
                                .producerInn("1234567890")
                                .productionDate("2020-01-23")
                                .tnvedCode("tnved")
                                .uitCode("uit")
                                .uituCode("uitu")
                                .build()
                ))
                .regDate("2020-01-23")
                .regNumber("reg1")
                .build();

        crptApid.createDocument(document.toString(), String.format(SIGNATURE, "Sergey"));
    }

}

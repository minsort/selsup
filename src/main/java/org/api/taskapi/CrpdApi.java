package org.api.taskapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class CrpdApi {
    private static final String SIGNATURE = "My signature: '%s'";

    public static void main(String[] args) {
        SpringApplication.run(CrpdApi.class, args);
        ServiceApi serviceApi = new ServiceApi(TimeUnit.MILLISECONDS, 2);

        DocumentRequestDto document = DocumentRequestDto.builder()
                .description(CrpdApi.DescriptionRequestDto.builder().participantInn("string").build())
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

        serviceApi.createDocument(document.toString(), String.format(SIGNATURE, "Sergey"));
    }

    public static class ServiceApi {
        private final HttpClient httpClient;
        private final Semaphore semaphore;
        private final long timeInterval;

        public ServiceApi(TimeUnit timeUnit, int requestLimit) {
            this.httpClient = HttpClient.newHttpClient();
            this.semaphore = new Semaphore(requestLimit);
            this.timeInterval = timeUnit.toMillis(1);
        }

        public void createDocument(String documentJson, String signature) {
            try {
                // Блокируем семафор, если доступных разрешений нет, поток будет заблокирован до тех пор, пока не будет доступно разрешение
                if (semaphore.tryAcquire(timeInterval, TimeUnit.MILLISECONDS)) {
                    // Создаем HTTP-запрос
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                            .header("Content-Type", "application/json")
                            .POST(HttpRequest.BodyPublishers.ofString(documentJson))
                            .build();

                    // Отправляем запрос и получаем ответ
                    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                    // Обрабатываем ответ
                    if (response.statusCode() == 200) {
                        // Запрос успешно выполнен
                        System.out.println("Документ успешно создан: " + response.body());
                    } else {
                        // Обработка ошибок
                        System.out.println("Не удалось создать документ. Код состояния: " + response.statusCode());
                    }
                } else {
                    // Обработка случая, когда достигнут лимит запросов в единицу времени
                    System.out.println("Превышен лимит запросов. Пожалуйста, повторите попытку позже.");
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                // Освобождаем разрешение после завершения запроса
                semaphore.release();
            }
        }

    }

    @Builder
    @AllArgsConstructor
    @Data
    public static class DescriptionRequestDto {
        @JsonProperty("participantInn")
        private String participantInn;
    }

    @AllArgsConstructor
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    public static class DocumentRequestDto {
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

    @Builder
    @Data
    public static class ProductRequestDto {

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

}

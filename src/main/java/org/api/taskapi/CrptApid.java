package org.api.taskapi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApid {
    private final HttpClient httpClient;
    private final Semaphore semaphore;
    private final long timeInterval;

    public CrptApid(TimeUnit timeUnit, int requestLimit) {
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
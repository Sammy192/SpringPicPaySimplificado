package com.picpaysimplificado.services;

import com.picpaysimplificado.entities.Transaction;
import com.picpaysimplificado.entities.User;
import com.picpaysimplificado.dtos.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) {
        try {
            String email = user.getEmail();
            NotificationDTO notificationRequest = new NotificationDTO(email, message);

            ResponseEntity<String> notificationResponse = restTemplate.postForEntity("https://util.devi.tools/api/v1/notify", notificationRequest, String.class);

            if(!(notificationResponse.getStatusCode() == HttpStatus.OK || notificationResponse.getStatusCode() == HttpStatus.NO_CONTENT )) {
                System.out.println("[NOTIFICANTION ERROR] Erro ao enviar notificação.");
                user.setStatusNotificationService("[NOTIFICANTION ERROR] Erro ao enviar notificação.");
               // throw new Exception("Serviço de notificação fora do ar");
            } else {
                System.out.println("[NOTIFICANTION SUCCESS] Notificação enviada para o usuario");
                user.setStatusNotificationService("[NOTIFICANTION SUCCESS] Notificação enviada para o usuario");
            }
        } catch (HttpServerErrorException e) {
            System.out.println("[NOTIFICANTION OFF] Servico notificação não disponível");
            user.setStatusNotificationService("[NOTIFICANTION OFF] Servico notificação não disponível");
        }


    }
}

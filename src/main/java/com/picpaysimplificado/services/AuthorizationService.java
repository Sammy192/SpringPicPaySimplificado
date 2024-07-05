package com.picpaysimplificado.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.ResponseAuthorizationDTO;
import com.picpaysimplificado.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class AuthorizationService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.authorizationApi}")
    private String authApiUrl;

    public boolean authorizeTransaction(User sender, BigDecimal value) throws Exception {
        try {
            ResponseEntity<ResponseAuthorizationDTO> authorizationResponse = restTemplate.getForEntity(authApiUrl, ResponseAuthorizationDTO.class);

            if (authorizationResponse.getStatusCode() == HttpStatus.OK &&
                    authorizationResponse.getBody() != null &&
                    "success".equalsIgnoreCase(authorizationResponse.getBody().status())) {
                return authorizationResponse.getBody().data().authorization();

            } else return false;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                return false; // Retorna false quando o status é 403
            } else {
                throw new Exception("Transação negada! Status: " + e.getStatusCode(), e);
            }
        } catch (Exception e) {
            throw new Exception("Erro ao autorizar transação", e);
        }

    }

}

package com.ead.authuser.clients;

import com.ead.authuser.api.dtos.response.CourseDTO;
import com.ead.authuser.api.dtos.response.ResponsePageDTO;
import com.ead.authuser.domain.services.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;
    private final UtilService utilService;

    String REQUEST_URI = "http://localhost:8082/api";

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> courseDTOS = null;
        ResponseEntity<ResponsePageDTO<CourseDTO>> result = null;

        String url = utilService.createUrl(userId, pageable);

        log.debug("Request URL: {} ", url);
        log.info("Request URL: {} ", url);

        try {
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType =
                    new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {
                    };

            result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);

            courseDTOS = result.getBody().getContent();

            log.debug("Response Number of Elements: {} ", courseDTOS.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request / courses {} ", e);
        }
        log.info("Ending request /course userId {} ", userId);
        return result.getBody();
    }
}

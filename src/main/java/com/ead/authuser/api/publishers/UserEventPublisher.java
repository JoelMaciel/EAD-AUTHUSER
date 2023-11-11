package com.ead.authuser.api.publishers;

import com.ead.authuser.api.dtos.event.UserEventDTO;
import com.ead.authuser.domain.enums.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    public final RabbitTemplate rabbitTemplate;
    @Value(value = "${ead.broker.exchange.userEvent}")
    private String exchangeUserEvent;

    public void publisherUserEvent(UserEventDTO userEventDTO, ActionType actionType) {
        userEventDTO.setActionType(actionType.toString());
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEventDTO);
    }
}

package com.atlassian.rp.controller;

import com.atlassian.rp.events.NotificationCreatedEventPublisher;
import com.atlassian.rp.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


@RestController
@Slf4j
public class SseNotificationController {
    private static final String SSE_NOTIFICATION_END_POINT_V1 = "/sse/v1/notifications";
    private static final String SSE_MESSAGE_EVENT_TYPE = "message";

    private final Flux<Notification> notificationCreatedEvents;

    @Autowired
    public SseNotificationController(NotificationCreatedEventPublisher notificationCreatedEventPublisher) {
        this.notificationCreatedEvents = notificationCreatedEventPublisher.getNotifications().share();
    }

    @GetMapping(value = SSE_NOTIFICATION_END_POINT_V1, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Notification>> notificationsFlux(ServerHttpResponse response) {
        setResponseHeaders(response);

        return notificationCreatedEvents.subscribeOn(Schedulers.boundedElastic())
                .map(notification ->
                        ServerSentEvent.<Notification>builder()
                                .event(SSE_MESSAGE_EVENT_TYPE)
                                .data(notification)
                                .build());
    }

    private void setResponseHeaders(ServerHttpResponse response) {
        // need this header due to a bug with react-app proxy
        // https://github.com/facebook/create-react-app/issues/1633#issuecomment-447436206
        response.getHeaders().add("Cache-Control", "no-transform");
    }
}

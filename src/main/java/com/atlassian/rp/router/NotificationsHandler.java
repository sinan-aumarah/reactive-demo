package com.atlassian.rp.router;

import com.atlassian.rp.domain.Notification;
import com.atlassian.rp.service.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@RequiredArgsConstructor
@Component
public class NotificationsHandler {

    private final @NonNull NotificationService notificationService;

    private static Mono<ServerResponse> notFoundResponse() {
        return ServerResponse.notFound().build();
    }

    public Mono<ServerResponse> getAllNotifications(ServerRequest serverRequest) {
        return okResponse().body(notificationService.findAll(), Notification.class);
    }

    public Mono<ServerResponse> getOneNotification(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Notification> notificationMono = notificationService.findById(id);
        return notificationMono
                .flatMap(notification -> okResponse().body(fromObject(notification)))
                .switchIfEmpty(notFoundResponse());
    }

    public Mono<ServerResponse> createNotification(ServerRequest serverRequest) {
        Mono<Notification> itemTobeInserted = serverRequest.bodyToMono(Notification.class);
        return itemTobeInserted.flatMap(notification -> okResponse()
                .body(notificationService.save(notification), Notification.class));
    }

    public Mono<ServerResponse> deleteNotification(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Void> deleteNotification = notificationService.deleteById(id);
        return okResponse().body(deleteNotification, Void.class);
    }

    public Mono<ServerResponse> updateNotification(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Notification> updatedNotification = serverRequest.bodyToMono(Notification.class)
                .flatMap((notification) -> notificationService.findById(id).flatMap(currentNotification -> {
                    currentNotification.setTitle(notification.getTitle());
                    currentNotification.setMessage(notification.getMessage());
                    return notificationService.save(currentNotification);
                }));
        return updatedNotification
                .flatMap(notification -> okResponse().body(fromObject(notification)))
                .switchIfEmpty(notFoundResponse());
    }

    private ServerResponse.BodyBuilder okResponse() {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON);
    }
}

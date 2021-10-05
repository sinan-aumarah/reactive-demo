package com.demo.rp.service;

import com.demo.rp.events.NotificationCreatedEventPublisher;
import com.demo.rp.domain.Notification;
import com.demo.rp.domain.NotificationReactiveRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationReactiveRepository notificationReactiveRepository;
    private final NotificationCreatedEventPublisher notificationCreatedEventPublisher;

    public Flux<Notification> findAll() {
        return notificationReactiveRepository.findAll();
    }

    public Mono<Notification> findById(String id) {
        return notificationReactiveRepository.findById(id);
    }

    public Mono<Notification> save(Notification notification) {
        return notificationReactiveRepository.save(notification)
                .doOnSuccess(this.notificationCreatedEventPublisher::emitNotification);
    }

    public Mono<Void> deleteById(String id) {
        return notificationReactiveRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return notificationReactiveRepository.deleteAll();
    }
}

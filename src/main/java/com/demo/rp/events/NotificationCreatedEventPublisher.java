package com.demo.rp.events;

import com.demo.rp.domain.Notification;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationCreatedEventPublisher {

    // The latest element pushed to this sink are remembered, even when there is no subscriber. Older elements are discarded
    // Backpressure : this sink honors downstream demand of individual subscribers.
    // Replaying: the latest element pushed to this sink is replayed to new subscribers
    private final @NonNull Sinks.Many<Notification> notifications = Sinks.many().replay().latest();

    /**
     * Provide a flux with our notifications.
     * @return a Flux
     */
    public Flux<Notification> getNotifications() {
        return notifications.asFlux();
    }

    /**
     * Emit a new event to the sink.
     * @param notification a notification to broadcast to all subscribers
     */
    public void emitNotification(Notification notification) {
        notifications.tryEmitNext(notification);
    }

}
package com.atlassian.rp.events;

import com.atlassian.rp.domain.Notification;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
public class NotificationEventPublisherBridge implements ApplicationListener<NotificationCreatedEvent>, Consumer<FluxSink<NotificationCreatedEvent>> {

    private final BlockingQueue<NotificationCreatedEvent> queue = new LinkedBlockingQueue<>();

    @Override
    public void onApplicationEvent(NotificationCreatedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<NotificationCreatedEvent> sink) {
        while (true)
            try {
                NotificationCreatedEvent event = queue.take();
                sink.next(event);
            } catch (InterruptedException e) {
                ReflectionUtils.rethrowRuntimeException(e);
            }
    }
}

class NotificationCreatedEvent extends ApplicationEvent {

    public NotificationCreatedEvent(Notification source) {
        super(source);
    }
}

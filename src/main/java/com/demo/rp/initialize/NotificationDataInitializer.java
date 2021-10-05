package com.demo.rp.initialize;

import com.demo.rp.service.NotificationService;
import com.github.javafaker.Faker;
import com.demo.rp.domain.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static java.lang.String.format;

@Component
@Profile("!test")
@Slf4j
public class NotificationDataInitializer implements CommandLineRunner {
    private static final Faker FAKER = Faker.instance();

    @Autowired
    NotificationService notificationService;

    @Override
    public void run(String... args) {
        initialDataSetUp();
    }

    public Flux<Notification> data() {
        return Flux.range(0, 1000)
                .delayElements(Duration.ofSeconds(3))
                .map(i -> {
                    String title = "New Kudos received!";
                    String message = format("%s has received Kudos", FAKER.name().fullName());
                    return new Notification(null, title, message);
                });
    }

    private void initialDataSetUp() {
        notificationService.deleteAll()
                .thenMany(Flux.from(data()))
                .flatMap(notificationService::save)
                .subscribe((notification -> {
                    System.out.println("Test data inserted: " + notification);
                }));
    }
}

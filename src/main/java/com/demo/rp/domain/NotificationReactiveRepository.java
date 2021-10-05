package com.demo.rp.domain;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NotificationReactiveRepository extends ReactiveMongoRepository<Notification, String> {

}

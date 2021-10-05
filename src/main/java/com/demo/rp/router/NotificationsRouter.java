package com.demo.rp.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class NotificationsRouter {
	private static final String NOTIFICATION_END_POINT_V1 = "/v1/notifications";

	@Bean
	public RouterFunction<ServerResponse> notificationsRoute(NotificationsHandler notificationsHandler) {
		return RouterFunctions
			.route(GET(NOTIFICATION_END_POINT_V1).and(accept(APPLICATION_JSON)),
				notificationsHandler::getAllNotifications)
			.andRoute(GET(NOTIFICATION_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
				notificationsHandler::getOneNotification)
			.andRoute(POST(NOTIFICATION_END_POINT_V1).and(accept(APPLICATION_JSON)),
				notificationsHandler::createNotification)
			.andRoute(DELETE(NOTIFICATION_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
				notificationsHandler::deleteNotification)
			.andRoute(PUT(NOTIFICATION_END_POINT_V1 + "/{id}").and(accept(APPLICATION_JSON)),
				notificationsHandler::updateNotification);
	}
}

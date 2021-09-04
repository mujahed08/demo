package com.example;

import io.micronaut.websocket.WebSocketBroadcaster;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@ServerWebSocket("/ws/broker/main/topic/{username}")
public class BrokerWebSocket {
	
	Logger logger = LoggerFactory.getLogger(BrokerWebSocket.class);
	
    private WebSocketBroadcaster broadcaster;
    
    public BrokerWebSocket(WebSocketBroadcaster broadcaster) {
        this.broadcaster = broadcaster;
    }

    @OnOpen
    public Publisher<String> onOpen(String username, WebSocketSession session) {
        String msg = "[" + username + "] Joined!";
        logger.info(msg);
        return broadcaster.broadcast(msg, skip(true));
    }

    @OnMessage
    public Publisher<String> onMessage(
            String message,
            WebSocketSession session) {
		logger.info(message);
        return broadcaster.broadcast(message, skip(false));
    }

    @OnClose
    public Publisher<String> onClose(String username, WebSocketSession session) {
        String msg = "[" + username + "] Disconnected!";
		logger.info(msg);
        return broadcaster.broadcast(msg, skip(true));
    }

    /*private Predicate<WebSocketSession> isValid(String topic) {
		logger.info("Topic: " + topic);
        return s -> topic.equalsIgnoreCase(s.getUriVariables().get("topic", String.class, null));
    }*/

    private Predicate<WebSocketSession> skip(final boolean skip) {
        return s -> !skip;
    }
}
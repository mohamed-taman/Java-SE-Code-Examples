/*
 * Copyright (c) 2018. SiriusX Innovations d.o.o.
 */

package com.tm.siriusxi.http.ws;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;
import static java.util.logging.Logger.getLogger;

/**
 * This class contains an example of http WebSocket
 * that can be followed to perform common tasks using
 * the Java HTTP Client WebSocket implementation.
 *
 * @author mohamed_taman
 * @version 1.0
 * @since Java 11
 */
public class WebSocketApp {

    private static final Logger logger = getLogger(WebSocketApp.class.getName());

    private static final ExecutorService executor = Executors.newFixedThreadPool(3);


    public static void main(String... args) throws InterruptedException {

        HttpClient httpClient = HttpClient.newBuilder().executor(executor).build();

        WebSocket webSocket = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create("ws://demos.kaazing.com/echo"),
                        new EchoListener(executor))
                .join();

        logger.info("WebSocket created");

        sleep(800);

        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "ok")
                .thenRun(() -> logger.info("Sent close"))
                .join();

    }
}

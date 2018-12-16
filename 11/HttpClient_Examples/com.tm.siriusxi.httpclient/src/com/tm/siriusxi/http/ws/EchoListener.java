/*
 * Copyright (c) 2018. SiriusX Innovations d.o.o.
 */

package com.tm.siriusxi.http.ws;

import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import static java.util.logging.Logger.*;

public final class EchoListener implements WebSocket.Listener {

    private static final Logger logger = getLogger(EchoListener.class.getName());

    private ExecutorService executor;

    public EchoListener(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        logger.info("CONNECTED");

        webSocket.sendText("This is a message", true);

        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        logger.info("Receiving Message -->");

        logger.info(String.format("onText received a data: %s", data));

        if (!webSocket.isOutputClosed()) {
            webSocket.sendText("This message should echoed back..", true);
        }

        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        logger.info("CLOSING");

        logger.info(String.format("Closed with status %d, and reason: %s", statusCode, reason));

        executor.shutdown();

        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
    }

}

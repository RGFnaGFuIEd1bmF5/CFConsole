package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.util.Deque;
import java.util.Map;

/**
 * Created by buraktutanlar on 02/10/15.
 */
public abstract class ContainerCmdHandler implements HttpHandler {

    public abstract void handleRequest(HttpServerExchange httpServerExchange) throws Exception;

    protected String getContainerID(HttpServerExchange exchange) {
        Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
        return queryParams.get("containerID").getFirst();
    }

    protected void sendNotFoundException(NotFoundException e, HttpServerExchange exchange) {
        String json = new DockerException("NotFoundException", e.getMessage()).toJSON();
        if (json == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
        }
        exchange.getResponseSender().send(json);
    }

    protected void sendNotModifiedException(NotModifiedException e, HttpServerExchange exchange) {
        String json = new DockerException("NotModifiedException", e.getMessage()).toJSON();
        if (json == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.NOT_MODIFIED);
        }
        exchange.getResponseSender().send(json);
    }
}

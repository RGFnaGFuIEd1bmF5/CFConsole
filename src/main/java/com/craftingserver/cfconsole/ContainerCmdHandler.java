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

    protected abstract void processCmd(String containerID) throws NotFoundException, NotModifiedException;

    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        String containerID = getContainerID(httpServerExchange);

        try {
            processCmd(containerID);
            sendEmpty200(httpServerExchange);
        } catch (NotFoundException e) {
            sendNotFoundException(e, httpServerExchange);
        } catch (NotModifiedException e) {
            sendNotModifiedException(e, httpServerExchange);
        }
    }

    private String getContainerID(HttpServerExchange exchange) {
        Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
        return queryParams.get("containerID").getFirst();
    }

    private void sendNotFoundException(NotFoundException e, HttpServerExchange exchange) {
        sendException(e, "NotFoundException", StatusCodes.NOT_FOUND, exchange);
    }

    private void sendNotModifiedException(NotModifiedException e, HttpServerExchange exchange) {
        sendException(e, "NotModifiedException", StatusCodes.NOT_MODIFIED, exchange);
    }

    private void sendException(Exception exception, String exceptionName, int statusCode, HttpServerExchange exchange) {
        String json = new DockerException(exceptionName, exception.getMessage()).toJSON();
        if (json == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(statusCode);
        }
        exchange.getResponseSender().send(json);
    }

    private void sendEmpty200(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseSender().send("");
    }
}

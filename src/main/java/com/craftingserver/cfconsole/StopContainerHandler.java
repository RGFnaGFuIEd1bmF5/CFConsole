package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.util.Deque;
import java.util.Map;

/**
 * Created by buraktutanlar on 01/10/15.
 */
public class StopContainerHandler extends ContainerCmdHandler {

    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        String containerID = getContainerID(httpServerExchange);

        try {
            DockerManager.getInstance().stopContainer(containerID);
            sendStoppedResponse(httpServerExchange);
        } catch (NotFoundException e) {
            sendNotFoundException(e, httpServerExchange);
        } catch (NotModifiedException e) {
            sendNotModifiedException(e, httpServerExchange);
        }
    }

    private void sendStoppedResponse(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseSender().send("");
    }
}

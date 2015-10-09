package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

/**
 * Created by buraktutanlar on 01/10/15.
 */
public class StartContainerHandler extends ContainerCmdHandler {

    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        String containerID = getContainerID(httpServerExchange);

        try {
            DockerManager.getInstance().startContainer(containerID);
            sendStartedResponse(httpServerExchange);
        } catch (NotFoundException e) {
            sendNotFoundException(e, httpServerExchange);
        } catch (NotModifiedException e) {
            sendNotModifiedException(e, httpServerExchange);
        }
    }

    private void sendStartedResponse(HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.OK);
        exchange.getResponseSender().send("");
    }
}

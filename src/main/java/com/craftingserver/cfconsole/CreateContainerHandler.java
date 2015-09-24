package com.craftingserver.cfconsole;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.util.Deque;
import java.util.Map;

/**
 * Created by buraktutanlar on 20/09/15.
 */
public class CreateContainerHandler implements HttpHandler {

    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        String gameID = getGameID(httpServerExchange);

        if (isGameIDValid(gameID)) {
            CreatedContainer createdContainer = createContainer(gameID);
            String createdContainerJSON = createdContainer.toJSON();
            sendCreatedContainerJSON(createdContainerJSON, httpServerExchange);
        } else {
            httpServerExchange.setStatusCode(StatusCodes.NOT_FOUND);
        }
    }

    private String getGameID(HttpServerExchange exchange) {
        Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
        return queryParams.get("gameID").getFirst();
    }

    private boolean isGameIDValid(String gameID) {
        if (gameID.equals("minecraft")) {
            return true;
        } else {
            System.out.println("This game couldn't recognized: " + gameID);
            return false;
        }
    }


    /**
     * Creates a container with provided gameID and returns created containerID.
     *
     * @param gameID
     * @return containerID
     */
    private CreatedContainer createContainer(String gameID) {
        String createCmdString = "itzg/minecraft-server";

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding(11022));
        portBindings.bind(tcp23, Ports.Binding(11023));

        CreateContainerResponse container = App.dockerClient
                .createContainerCmd(createCmdString)
                .withExposedPorts(tcp22, tcp23)
                .exec();

        App.dockerClient.startContainerCmd(container.getId()).exec();

        // TODO these variables must be retrived to create CreatedContainer
        String host = "This must be retrieved back from dockerClient";
        int exposedPort = 3131;//"This must be one of the ports defined above"
        String contianerID = container.getId();

        return new CreatedContainer(host, exposedPort, contianerID);
    }

    private void sendCreatedContainerJSON(String createdContainerJSON, HttpServerExchange exchange) {
        if (createdContainerJSON == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.CREATED);
        }
        exchange.getResponseSender().send(createdContainerJSON);
    }

}

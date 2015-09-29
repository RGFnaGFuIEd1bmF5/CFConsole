package com.craftingserver.cfconsole;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.InternetProtocol;
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

        String createContainerCmdArg = "itzg/minecraft-server";

        CreateContainerResponse container = App.dockerClient.createContainerCmd(createContainerCmdArg)
                .withEnv("EULA=TRUE")
                .withPublishAllPorts(true)
                .exec();

        App.dockerClient.startContainerCmd(container.getId()).exec();

        String host = getDockerHost();
        int exposedPort = getExposedPort(container.getId());

        return new CreatedContainer(host, exposedPort, container.getId());
    }

    private String getDockerHost() {
        return App.dockerClientConfig.getUri().getHost();
    }

    private int getExposedPort(String containerID) {
        InspectContainerResponse containerInfo = App.dockerClient.inspectContainerCmd(containerID).exec();
        Map<ExposedPort, Ports.Binding[]> bindings = containerInfo.getNetworkSettings().getPorts().getBindings();
        ExposedPort port = new ExposedPort(25565, InternetProtocol.TCP);
        Ports.Binding[] bindingArray = bindings.get(port);

        return bindingArray[0].getHostPort();
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

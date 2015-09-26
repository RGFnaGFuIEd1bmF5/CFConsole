package com.craftingserver.cfconsole;

import com.github.dockerjava.api.DockerClient;
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

    private DockerClient dockerClient;

    public CreateContainerHandler(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {
        System.out.println(this.createContainer());
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
    private String createContainer() {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        CreateContainerResponse container = dockerClient.createContainerCmd("itzg/minecraft-server")
                .withCmd("sleep", "9999")
                .withCmd("interactive", "true")
                .withCmd("tty", "true")
                .withEnv("EULA", "TRUE")
                .withEnv("true")
                .withExposedPorts(tcp22, tcp23)
                .exec();

        return container.getId();
    }
}

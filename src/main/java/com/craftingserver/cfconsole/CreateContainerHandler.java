package com.craftingserver.cfconsole;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
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

        ExposedPort tcp22 = ExposedPort.tcp(25565);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding(25565));

        CreateContainerResponse container = dockerClient.createContainerCmd("itzg/minecraft-server")
                .withEnv("EULA=TRUE","VERSION=LATEST").withCmd("/start")
                .withExposedPorts(tcp22).withPortBindings(portBindings).withPublishAllPorts(true).exec();

        dockerClient.startContainerCmd(container.getId()).exec();
//        ,"interactive", "true","sleep", "9999","name","mc","env", "[EULA=TRUE]"

        return container.getId();
    }
}

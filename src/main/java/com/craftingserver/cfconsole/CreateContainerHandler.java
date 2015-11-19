package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
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
            tryToProvideNewContainer(gameID, httpServerExchange);
        } else {
            sendUnsupportedGameID(gameID, httpServerExchange);
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

    private void tryToProvideNewContainer(String gameID, HttpServerExchange httpServerExchange) {
        try {
            CreatedContainer createdContainer = createContainer(gameID);
            sendCreatedContainer(createdContainer, httpServerExchange);
        } catch (NotFoundException e) {
            sendNotFoundException(e, httpServerExchange);
        } catch (NotModifiedException e) {
            sendNotModifiedException(e, httpServerExchange);
        }
    }

    /**
     * Creates a container with provided gameID and returns created containerID
     *
     * @param gameID
     * @return containerID
     * @throws NotFoundException
     * @throws NotModifiedException
     */
    private CreatedContainer createContainer(String gameID) throws NotFoundException, NotModifiedException {
        String imageName = "itzg/minecraft-server";
        String containerID = DockerManager.getInstance().createContainer(imageName);
        DockerManager.getInstance().startContainer(containerID);
        String host = getDockerHost();

        // 25565 is the default listening port of minecraft game server.
        int exposedPort = getExposedPort(containerID, 25565);

        return new CreatedContainer(host, exposedPort, containerID);
    }

    private String getDockerHost() {
        return DockerManager.getInstance().getDockerHost();
    }

    /**
     * For More Info: Look at javadoc of getExposedPort(String, int) function in DockerManager.
     *
     * @param containerID
     * @param defaultPort
     * @return
     */
    private int getExposedPort(String containerID, int defaultPort) {
        return DockerManager.getInstance().getExposedPort(containerID, defaultPort);
    }

    private void sendCreatedContainer(CreatedContainer createdContainer, HttpServerExchange exchange) {
        String createdContainerJSON = createdContainer.toJSON();
        if (createdContainerJSON == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.CREATED);
        }
        exchange.getResponseSender().send(createdContainerJSON);
    }

    private void sendNotFoundException(NotFoundException e, HttpServerExchange exchange) {
        String json = new DockerException("NotFoundException", e.getMessage()).toJSON();
        if (json == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.NOT_FOUND);
        }
        exchange.getResponseSender().send(json);
    }

    private void sendNotModifiedException(NotModifiedException e, HttpServerExchange exchange) {
        String json = new DockerException("NotModifiedException", e.getMessage()).toJSON();
        if (json == null) {
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
        } else {
            exchange.setStatusCode(StatusCodes.NOT_MODIFIED);
        }
        exchange.getResponseSender().send(json);
    }

    // TODO this function requires more attention...
    private void sendUnsupportedGameID(String gameID, HttpServerExchange exchange) {
        exchange.setStatusCode(StatusCodes.NOT_IMPLEMENTED);
        exchange.getResponseSender().send("{" + gameID + ": Not implemented gameID}");
    }

}

package com.craftingserver.cfconsole;

import io.undertow.Undertow;
import io.undertow.server.handlers.PathTemplateHandler;

/**
 * Created by buraktutanlar on 20/09/15.
 */
public class App {

    public static void main(final String[] args) throws Exception {
        DockerManager.getInstance(); // to init DockerManager singleton
        initServer();
    }

    private static void initServer() {
        Undertow server = Undertow.builder().addHttpListener(8081, "localhost")
                .setHandler(getMyPathTemplateHandler()).build();
        server.start();
    }

    private static PathTemplateHandler getMyPathTemplateHandler() {
        PathTemplateHandler handler = new PathTemplateHandler();
        handler.add("/container/create/{gameID}", new CreateContainerHandler());
        handler.add("/container/start/{containerID}", new StartContainerHandler());
        handler.add("/container/stop/{containerID}", new StopContainerHandler());
        handler.add("/container/pause/{containerID}", new PauseContainerHandler());
        handler.add("/container/unpause/{containerID}", new UnpauseContainerHandler();
        return handler;
    }
}

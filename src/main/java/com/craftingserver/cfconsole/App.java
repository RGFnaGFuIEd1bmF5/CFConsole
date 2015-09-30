package com.craftingserver.cfconsole;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathTemplateHandler;

import java.util.List;

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
        return handler;
    }
}

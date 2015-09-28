package com.craftingserver.cfconsole;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathTemplateHandler;

import java.util.List;

/**
 * Created by buraktutanlar on 20/09/15.
 */
public class App {


    private static DockerClient dockerClient;

    public static void main(final String[] args) {

        initDockerClient();
        initServer();
    }

    private static void initDockerClient() {
        DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion("1.12")
                .withUri("http://178.62.65.18:3131")
                .withUsername("craftingserver")
                .withPassword("asdfghjkl")
                .withEmail("burak.tutanlar@gmail.com")
                .withServerAddress("https://index.docker.io/v1/")
                .withDockerCertPath("/home/user/.docker")
                .build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
    }

    private static void initServer() {
        Undertow server = Undertow.builder().addHttpListener(8081, "localhost")
                .setHandler(getMyPathTemplateHandler()).build();
        server.start();
    }

    private static PathTemplateHandler getMyPathTemplateHandler() {
        PathTemplateHandler handler = new PathTemplateHandler();
        handler.add("/container/create", new CreateContainerHandler(dockerClient));
        return handler;
    }
}


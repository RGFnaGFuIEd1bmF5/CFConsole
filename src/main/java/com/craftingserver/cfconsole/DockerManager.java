package com.craftingserver.cfconsole;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.InternetProtocol;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by buraktutanlar on 30/09/15.
 */
public class DockerManager {

    private DockerClientConfig dockerClientConfig;
    private DockerClient dockerClient;

    private static DockerManager instance;

    public static DockerManager getInstance() {
        if (instance == null) {
            return new DockerManager();
        } else {
            return instance;
        }
    }

    private DockerManager() {
        dockerClientConfig = DockerClientConfig.createDefaultConfigBuilder()
                .withVersion("1.12")
                .withUri("http://85.97.116.219:3131")
                .withUsername("craftingserver")
                .withPassword("asdfghjkl")
                .withEmail("burak.tutanlar@gmail.com")
                .withServerAddress("https://index.docker.io/v1/")
                .withDockerCertPath("/home/user/.docker")
                .build();
        dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();
    }

    public String createContainer(String imageName) {
        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withEnv("EULA=TRUE")
                .withPublishAllPorts(true)
                .exec();

        return container.getId();
    }

    public void startContainer(String containerID) throws NotFoundException, NotModifiedException {
        dockerClient.startContainerCmd(containerID).exec();
    }

    public void stopContainer(String containerID) throws NotFoundException, NotModifiedException {
        dockerClient.stopContainerCmd(containerID).exec();
    }

    public void pauseContainer(String containerID) throws  NotFoundException {
        dockerClient.pauseContainerCmd(containerID).exec();
    }

    public void unpauseContainer(String containerID) throws  NotFoundException {
        dockerClient.unpauseContainerCmd(containerID).exec();
    }

    public void killContainer(String containerID) throws NotFoundException {
        dockerClient.killContainerCmd(containerID).exec();
    }

    public void removeContainer(String containerID) throws NotFoundException {
        dockerClient.removeContainerCmd(containerID).exec();
    }

    public String getDockerHost() {
        return dockerClientConfig.getUri().getHost();
    }

    /**
     *
     * @param containerID
     * @param defaultPort: default listening port of game server.
     * @return exposedPort: port that docker bound to default listening port of game server.
     */
    public int getExposedPort(String containerID, int defaultPort) {
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(containerID).exec();
        Map<ExposedPort, Ports.Binding[]> bindings = containerInfo.getNetworkSettings().getPorts().getBindings();
        ExposedPort port = new ExposedPort(defaultPort, InternetProtocol.TCP);
        Ports.Binding[] bindingArray = bindings.get(port);

        // be careful about this 0 index selection blindly
        return bindingArray[0].getHostPort();
    }

    public void deleteAllContainers() {
        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        for (Container container : containers) {
            dockerClient.removeContainerCmd(container.getId()).exec();
        }
    }
}

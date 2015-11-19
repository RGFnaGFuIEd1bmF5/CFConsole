package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

/**
 * Created by buraktutanlar on 01/10/15.
 */
public class StartContainerHandler extends ContainerCmdHandler {

    @Override
    protected void processCmd(String containerID) throws NotFoundException, NotModifiedException {
        DockerManager.getInstance().startContainer(containerID);
    }
}

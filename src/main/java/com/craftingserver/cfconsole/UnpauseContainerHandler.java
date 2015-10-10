package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;

/**
 * Created by buraktutanlar on 10/10/15.
 */
public class UnpauseContainerHandler extends ContainerCmdHandler {

    @Override
    protected void processCmd(String containerID) throws NotFoundException, NotModifiedException {
        DockerManager.getInstance().unpauseContainer(containerID);
    }
}

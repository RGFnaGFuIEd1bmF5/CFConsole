package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;

/**
 * Created by buraktutanlar on 07/12/15.
 */
public class RestartContainerHandler extends ContainerCmdHandler {

    @Override
    protected void processCmd(String containerID) throws NotFoundException, NotModifiedException {
        DockerManager.getInstance().restartContainer(containerID);
    }
}

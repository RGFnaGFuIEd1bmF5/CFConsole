package com.craftingserver.cfconsole;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;

/**
 * Created by buraktutanlar on 23/11/15.
 */
public class KillContainerHandler extends ContainerCmdHandler {
    @Override
    protected void processCmd(String containerID) throws NotFoundException {
        DockerManager.getInstance().killContainer(containerID);
    }
}

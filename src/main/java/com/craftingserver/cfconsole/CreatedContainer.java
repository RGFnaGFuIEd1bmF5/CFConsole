package com.craftingserver.cfconsole;

/**
 * Created by buraktutanlar on 24/09/15.
 */
public class CreatedContainer extends JSONModel {

    private String host;
    private int exposedPort;
    private String containerID;

    public CreatedContainer(String host, int exposedPort, String containerID) {
        this.host = host;
        this.exposedPort = exposedPort;
        this.containerID = containerID;
    }

    public String getHost() {
        return this.host;
    }

    public int getExposedPort() {
        return this.exposedPort;
    }

    public String getContainerID() {
        return this.containerID;
    }

}

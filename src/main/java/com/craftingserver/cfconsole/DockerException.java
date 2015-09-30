package com.craftingserver.cfconsole;

/**
 * Created by buraktutanlar on 30/09/15.
 */
public class DockerException extends JSONModel {

    private String exceptionName;
    private String message;

    public DockerException(String exceptionName, String message) {
        this.exceptionName = exceptionName;
        this.message = message;
    }

    public String getExceptionName() {
        return this.exceptionName;
    }

    public String getMessage() {
        return this.message;
    }

}

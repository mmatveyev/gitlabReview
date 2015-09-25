package com.cloudmccloud.gitlabReview.connection.exceptions;

/**
 * Created by maxm on 25.09.15.
 */
public class RemoteApiLoginException extends RemoteApiException {
    public RemoteApiLoginException(String message) {
        super(message);
    }

    public RemoteApiLoginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

package com.cloudmccloud.gitlabReview.connection.exceptions;

/**
 * Created by maxm on 25.09.15.
 */
public class RemoteApiException extends Exception {
    public RemoteApiException(String message) {
        super(message);
    }

    public RemoteApiException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

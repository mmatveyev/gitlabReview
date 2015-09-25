package com.cloudmccloud.gitlabReview.connection;

import com.cloudmccloud.gitlabReview.configuration.GitlabReviewSettings;
import com.cloudmccloud.gitlabReview.connection.exceptions.RemoteApiException;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.TokenType;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabSession;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

public class GitlabConnector {
    public enum ConnectionState {
        NOT_FIFNSHED,
        SUCCEEDED,
        FAILED,
        INTERRUPTED
    }

    private ConnectionState myConnectionState = ConnectionState.NOT_FIFNSHED;
    private Exception myException;
    private final Project myProject;
    private static final Logger LOG = Logger.getInstance(TestConnectionTask.class.getName());

    private GitlabSession api;

    public GitlabConnector(Project project) {
        myProject = project;
    }

    public ConnectionState getConnectionState() {
        return myConnectionState;
    }

    public void run() {
        try {
            connect();
            if (myConnectionState != ConnectionState.INTERRUPTED) {
                myConnectionState = ConnectionState.SUCCEEDED;
            }
        }
        catch (RemoteApiException e) {
            if (myConnectionState != ConnectionState.INTERRUPTED) {
                myConnectionState = ConnectionState.FAILED;
                myException = e;
            }
        }
    }

    public void setInterrupted() {
        myConnectionState = ConnectionState.INTERRUPTED;
    }

    @Nullable
    public String getErrorMessage() {
        return myException == null ? null : myException.getMessage();
    }

    public void connect() throws RemoteApiException {

        try {
            api = GitlabAPI.connect(getServerUrl(), getUserName(), getPassword());
            String token = api.getPrivateToken();
            GitlabAPI newApi = GitlabAPI.connect(getServerUrl(), token, TokenType.PRIVATE_TOKEN);
            List<GitlabProject> list = newApi.getProjects();

            for (GitlabProject project : list) {
                LOG.warn(project.getName());
            }

            myConnectionState = ConnectionState.SUCCEEDED;
        } catch (IOException e) {
            myConnectionState = ConnectionState.FAILED;
            throw new RemoteApiException(e.getMessage());
        }
    }

    private String getServerUrl() {
        return GitlabReviewSettings.getInstance(myProject).SERVER_URL;
    }

    private String getApiToken() {
        return GitlabReviewSettings.getInstance(myProject).API_TOKEN;
    }

    private String getUserName() {
        return GitlabReviewSettings.getInstance(myProject).USERNAME;
    }

    private String getPassword() {
        return GitlabReviewSettings.getInstance(myProject).PASSWORD;
    }
}

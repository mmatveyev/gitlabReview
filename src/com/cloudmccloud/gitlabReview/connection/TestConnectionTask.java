package com.cloudmccloud.gitlabReview.connection;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TestConnectionTask extends Task.Modal {
    private static final int CHECK_CANCEL_INTERVAL = 500;
    private static final Logger LOG = Logger.getInstance(TestConnectionTask.class.getName());
    public TestConnectionTask(Project project, String title, boolean canBeCancelled) {
        super(project, title, canBeCancelled);
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setText("Connecting...");
        indicator.setFraction(0);
        indicator.setIndeterminate(true);

        final GitlabConnector connector = new GitlabConnector(myProject);
        connector.run();

        while(connector.getConnectionState() == GitlabConnector.ConnectionState.NOT_FIFNSHED) {
            try {
                if (indicator.isCanceled()) {
                    connector.setInterrupted();
                    break;
                }
            }
            catch (Exception e) {
                LOG.info(e.getMessage());
            }
        }

        GitlabConnector.ConnectionState state = connector.getConnectionState();
        switch (state) {
            case FAILED:
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Messages.showDialog(myProject, connector.getErrorMessage(), "Authentication Error", new String[]{"Ok"}, 0, null);
                    }
                });
                break;
            case INTERRUPTED:
                LOG.debug("'Test connection' canceled");
                break;
            case SUCCEEDED:
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Messages.showDialog(myProject, "Connected successfully", "Connection OK", new String[]{"OK"}, 0, null);
                    }
                });
                break;
            default: //NOT_FINISHED
                LOG.warn("Unexpected 'Test Connection' state: " + connector.getConnectionState().toString());
        }


    }
}

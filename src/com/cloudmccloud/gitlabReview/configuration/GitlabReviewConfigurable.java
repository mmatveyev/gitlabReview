package com.cloudmccloud.gitlabReview.configuration;

import com.cloudmccloud.gitlabReview.connection.TestConnectionTask;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GitlabReviewConfigurable implements SearchableConfigurable {
    private final Project myProject;
    private JPanel mainPannel;
    private JTextField serverUrlField;
    private JTextField apiTokenField;
    private JButton testConnectionButton;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private GitlabReviewSettings myGitlabReviewSettings;

    public GitlabReviewConfigurable(Project project) {
        myProject = project;
        myGitlabReviewSettings = GitlabReviewSettings.getInstance(myProject);

        testConnectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSettings();
                Task.Modal testConnectionTask = new TestConnectionTask(myProject, "TestingConnection", true);
                testConnectionTask.setCancelText("Stop");
                ProgressManager.getInstance().run(testConnectionTask);
            }
        });
    }

    private void saveSettings() {
        myGitlabReviewSettings.SERVER_URL = serverUrlField.getText();
        myGitlabReviewSettings.API_TOKEN = apiTokenField.getText();
        myGitlabReviewSettings.USERNAME = userNameField.getText();
        myGitlabReviewSettings.PASSWORD = new String(passwordField.getPassword());
    }

    @NotNull
    @Override
    public String getId() {
        return "GitlabReviewConfigurable";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String option) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "GitlabReview Connector";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPannel;
    }

    @Override
    public boolean isModified() {
        if (!StringUtil.equals(myGitlabReviewSettings.SERVER_URL, serverUrlField.getText())) {
            return true;
        }
        if (!StringUtil.equals(myGitlabReviewSettings.API_TOKEN, apiTokenField.getText())) {
            return true;
        }
        if (!StringUtil.equals(myGitlabReviewSettings.USERNAME, userNameField.getText())) {
            return true;
        }
        if (!StringUtil.equals(myGitlabReviewSettings.PASSWORD, new String(passwordField.getPassword()))) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        saveSettings();
    }

    @Override
    public void reset() {
        serverUrlField.setText(myGitlabReviewSettings.SERVER_URL);
        apiTokenField.setText(myGitlabReviewSettings.API_TOKEN);
        userNameField.setText(myGitlabReviewSettings.USERNAME);
        passwordField.setText(myGitlabReviewSettings.PASSWORD);
    }

    @Override
    public void disposeUIResources() {

    }
}

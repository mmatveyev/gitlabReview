package com.cloudmccloud.gitlabReview;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GitlabReviewConfigurable implements SearchableConfigurable {
    private final Project myProject;
    private JPanel mainPannel;
    private JTextField serverUrlField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox RememberPassword;
    private JButton testConnectionButton;
    private GitlabReviewSettings myGitlabReviewSettings;

    public GitlabReviewConfigurable(Project project) {
        myProject = project;
        myGitlabReviewSettings = GitlabReviewSettings.getInstance(myProject);
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
        if (!StringUtil.equals(myGitlabReviewSettings.PASSWORD, new String(passwordField.getPassword()))) {
            return true;
        }
        if (!StringUtil.equals(myGitlabReviewSettings.SERVER_URL, serverUrlField.getText())) {
            return true;
        }
        if (!StringUtil.equals(myGitlabReviewSettings.USERNAME, usernameField.getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        myGitlabReviewSettings.USERNAME = usernameField.getText();
        myGitlabReviewSettings.SERVER_URL = serverUrlField.getText();
        myGitlabReviewSettings.PASSWORD = new String(passwordField.getPassword());
    }

    @Override
    public void reset() {
        usernameField.setText(myGitlabReviewSettings.USERNAME);
        serverUrlField.setText(myGitlabReviewSettings.SERVER_URL);
        passwordField.setText(myGitlabReviewSettings.PASSWORD);
    }

    @Override
    public void disposeUIResources() {

    }
}

package com.cloudmccloud;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by maxm on 21.08.15.
 */
public class GitlabReviewConfigurable implements SearchableConfigurable {
    private final Project myProject;
    private JPanel myMainPanel;
    private JTextField myServerField;
    private JTextField myUsernameField;
    private JPasswordField myPasswordField;
    private JCheckBox checkRememberPassword;
    private JButton testButton;
    private GitlabReviewSettings myGitlabSettings;

    public GitlabReviewConfigurable(Project myProject) {
        this.myProject = myProject;
        myGitlabSettings = GitlabReviewSettings.getInstance(myProject);
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
        return myMainPanel;
    }

    @Override
    public boolean isModified() {
        if (!StringUtil.equals(myGitlabSettings.PASSWORD, new String(myPasswordField.getPassword()))) {
            return true;
        }
        if (!StringUtil.equals(myGitlabSettings.SERVER_URL, myServerField.getText())) {
            return true;
        }
        if(!StringUtil.equals(myGitlabSettings.USERNAME, myUsernameField.getText())) {
            return true;
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        myGitlabSettings.USERNAME = myUsernameField.getText();
        myGitlabSettings.PASSWORD = new String(myPasswordField.getPassword());
        myGitlabSettings.SERVER_URL = myServerField.getText();
    }

    @Override
    public void reset() {
        myUsernameField.setText(myGitlabSettings.USERNAME);
        myPasswordField.setText(myGitlabSettings.PASSWORD);
        myServerField.setText(myGitlabSettings.SERVER_URL);
    }

    @Override
    public void disposeUIResources() {

    }
}

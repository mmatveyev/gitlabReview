package com.cloudmccloud.gitlabReview;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "CrucibleSettings",
        storages = {
                @Storage( file = StoragePathMacros.PROJECT_FILE),
                @Storage( file = StoragePathMacros.PROJECT_CONFIG_DIR + "/crucibleConnector.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class GitlabReviewSettings implements PersistentStateComponent<GitlabReviewSettings> {
    public String SERVER_URL = "";
    public String USERNAME = "";
    public String PASSWORD = "";
    @Override
    public GitlabReviewSettings getState() {
        return this;
    }

    @Override
    public void loadState(GitlabReviewSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static GitlabReviewSettings getInstance(Project project) {
        return ServiceManager.getService(project, GitlabReviewSettings.class);
    }
}

package com.kungyu.action.project;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/8 18:00
 * @Version 1.0
 */
public class ProjectSdkAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project != null) {
            Sdk projectSdk = ProjectRootManager.getInstance(project).getProjectSdk();
            if (projectSdk != null) {
                String origProjectSdkName = projectSdk.getName();
                WriteCommandAction.runWriteCommandAction(project,() ->
                        ProjectRootManager.getInstance(project).setProjectSdkName("newProjectSdkName","")
                );
                Messages.showInfoMessage("origProjectSdkName:" + origProjectSdkName,"Change Project SDK Name Success");
            } else {
                Messages.showInfoMessage("Unknown Project SDK", "Change Project SDK Name Fail");
            }
        } else {
            Messages.showInfoMessage("Unknown Project", "Change Project SDK Name Fail");
        }
    }
}
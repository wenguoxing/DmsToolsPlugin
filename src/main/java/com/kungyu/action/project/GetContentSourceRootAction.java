package com.kungyu.action.project;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/8 17:58
 * @Version 1.0
 */
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wengyongcheng
 * @since 2020/3/17 4:07 下午
 * 获取工程下所有模块的Source Root
 */
public class GetContentSourceRootAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile[] virtualFiles = ProjectRootManager.getInstance(project).getContentSourceRoots();
        String sourceRoot = Arrays.stream(virtualFiles).map(VirtualFile::getUrl).collect(Collectors.joining("\n"));
        Messages.showInfoMessage("Source roots for project: " + project.getName() + " are " + sourceRoot,"Project Properties");
    }
}
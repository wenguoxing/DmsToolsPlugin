package com.kungyu.action.project;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/8 17:59
 * @Version 1.0
 */
public class ProjectFileIndexAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        if (project == null || editor == null) {
            return;
        }
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);
        if (file == null) {
            Messages.showInfoMessage("Unknown File", "Project File Index");
        } else {
            ProjectFileIndex fileIndex = ProjectFileIndex.getInstance(project);
            Messages.showInfoMessage("Module: " + fileIndex.getModuleForFile(file) + "\n" +
                            "Is In Source: " + fileIndex.isInSource(file) + "\n" +
                            "Module content root: " + fileIndex.getContentRootForFile(file) + "\n" +
                            "Source root: " + fileIndex.getSourceRootForFile(file) + "\n" +
                            "Is library file: " + fileIndex.isLibraryClassFile(file) + "\n" +
                            "Is in library classes: " + fileIndex.isInLibraryClasses(file) +
                            ", Is in library source: " + fileIndex.isInLibrarySource(file),
                    "Main File Info for" + file.getName());
        }
    }
}
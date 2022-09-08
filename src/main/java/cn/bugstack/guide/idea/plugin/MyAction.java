package cn.bugstack.guide.idea.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/5 9:31
 * @Version 1.0
 */
public class MyAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String classPath = psiFile.getVirtualFile().getPath();

        Messages.showMessageDialog(project, "guide-idea-plugin-create-project-by-gradle: " + classPath, "Hi IDEA Plugin", Messages.getInformationIcon());
    }

}
package cn.bugstack.guide.idea.plugin.action;

import cn.bugstack.guide.idea.plugin.service.IProjectGenerator;
import cn.bugstack.guide.idea.plugin.service.impl.ProjectGeneratorImpl;
import cn.bugstack.guide.idea.plugin.ui.DmsCpToolUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/6 15:20
 * @Version 1.0
 */
public class DmsCpToolAPBatchAction extends AnAction {

    private IProjectGenerator projectGenerator = new ProjectGeneratorImpl();

    public DmsCpToolAPBatchAction() {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        String classPath = psiFile.getVirtualFile().getPath();

        Editor editor = e.getData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();
        VirtualFile file = FileDocumentManager.getInstance().getFile(document);

        DmsCpToolUI cpToolUI = new DmsCpToolUI(project,psiFile,file,3);
        //是否允许用户通过拖拽的方式扩大或缩小你的表单框，我这里定义为true，表示允许
        cpToolUI.setResizable(true);
        cpToolUI.show();
    }
}
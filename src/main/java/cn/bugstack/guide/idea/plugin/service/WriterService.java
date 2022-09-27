package cn.bugstack.guide.idea.plugin.service;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.ThrowableRunnable;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 11:06
 * @Version 1.0
 */
public class WriterService {

    public void write(Project project, PsiElement psiElement, PsiDocComment comment) {
        try {
            WriteCommandAction.writeCommandAction(project).run(
                    (ThrowableRunnable<Throwable>)() -> {
                        if (psiElement.getContainingFile() == null) {
                            return;
                        }

                        // 写入文档注释
                        if (psiElement instanceof PsiJavaDocumentedElement) {
                            PsiDocComment psiDocComment = ((PsiJavaDocumentedElement)psiElement).getDocComment();
                            if (psiDocComment == null) {
                                psiElement.getNode().addChild(comment.getNode(), psiElement.getFirstChild().getNode());
                            } else {
                                psiDocComment.replace(comment);
                            }
                        }

                        // 格式化文档注释
                        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(psiElement.getProject());
                        PsiElement javadocElement = psiElement.getFirstChild();
                        int startOffset = javadocElement.getTextOffset();
                        int endOffset = javadocElement.getTextOffset() + javadocElement.getText().length();
                        codeStyleManager.reformatText(psiElement.getContainingFile(), startOffset, endOffset + 1);
                    });
        } catch (Throwable throwable) {
            //LOGGER.error("写入错误", throwable);
        }
    }

    public void write(Project project, Editor editor, String text) {
        if (project == null || editor == null || StringUtils.isBlank(text)) {
            return;
        }
        try {
            WriteCommandAction.writeCommandAction(project).run(
                    (ThrowableRunnable<Throwable>)() -> {
                        int start = editor.getSelectionModel().getSelectionStart();
                        EditorModificationUtil.insertStringAtCaret(editor, text);
                        editor.getSelectionModel().setSelection(start, start + text.length());
                    });
        } catch (Throwable throwable) {
            //LOGGER.error("写入错误", throwable);
        }
    }

    /**
     * 构造函数
     */
    public WriterService() {
    }
}
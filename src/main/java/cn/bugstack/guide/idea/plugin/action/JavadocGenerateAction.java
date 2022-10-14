package cn.bugstack.guide.idea.plugin.action;

import cn.bugstack.guide.idea.plugin.listener.AppActivationListener;
import cn.bugstack.guide.idea.plugin.service.DocGeneratorService;
import cn.bugstack.guide.idea.plugin.service.WriterService;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.util.messages.MessageBusConnection;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/26 14:47
 * @Version 1.0
 */
public class JavadocGenerateAction extends AnAction {

    /** 生成注释格式服务 */
    private DocGeneratorService docGeneratorService = ServiceManager.getService(DocGeneratorService.class);
    /** 写入注释服务 */
    private WriterService writerService = ServiceManager.getService(WriterService.class);

    /**
     * 构造函数
     */
    public JavadocGenerateAction() {
        super();
        Application app = ApplicationManager.getApplication();
        Disposable disposable = Disposer.newDisposable();
        Disposer.register(app, disposable);

        //通知
        MessageBusConnection connection = app.getMessageBus().connect(disposable);
        connection.subscribe(ApplicationActivationListener.TOPIC, new AppActivationListener());
    }

    /**
     * actionPerformed
     *
     * @param e e
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(LangDataKeys.PROJECT);
        if (null == project) {
            return;
        }

        PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if (psiElement == null || psiElement.getNode() == null) {
            return;
        }

        //生成注释
        String comment = docGeneratorService.generate(psiElement);
        if (StringUtils.isEmpty(comment)) {
            return;
        }

        //获取注释
        PsiElementFactory factory = PsiElementFactory.SERVICE.getInstance(project);
        PsiDocComment psiDocComment = factory.createDocCommentFromText(comment);

        //写入注释
        writerService.write(project, psiElement, psiDocComment);
    }
}
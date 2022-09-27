package cn.bugstack.guide.idea.plugin.action;

import cn.bugstack.guide.idea.plugin.service.IProjectGenerator;
import cn.bugstack.guide.idea.plugin.service.impl.ProjectGeneratorImpl;
import cn.bugstack.guide.idea.plugin.ui.ORMSettingsUI;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;

public class CodeGenerateAction extends AnAction {

    private IProjectGenerator projectGenerator = new ProjectGeneratorImpl();

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        ShowSettingsUtil.getInstance().editConfigurable(project, new ORMSettingsUI(project, projectGenerator));
    }

}

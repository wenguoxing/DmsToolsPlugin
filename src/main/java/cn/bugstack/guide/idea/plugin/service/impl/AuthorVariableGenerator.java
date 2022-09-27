package cn.bugstack.guide.idea.plugin.service.impl;

import cn.bugstack.guide.idea.plugin.config.JavaDocConfigComponent;
import cn.bugstack.guide.idea.plugin.service.VariableGenerator;
import cn.bugstack.guide.idea.plugin.config.JavaDocConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;

/**
 * @author <a href="mailto:wenguoxing@126.com">wenguoxing</a>
 * @version 1.0.0
 * @since 2019-12-07 23:16:00
 */
public class AuthorVariableGenerator implements VariableGenerator {
    private JavaDocConfiguration config = ServiceManager.getService(JavaDocConfigComponent.class).getState();

    @Override
    public String generate(PsiElement element) {
        return config.getAuthor();
    }
}
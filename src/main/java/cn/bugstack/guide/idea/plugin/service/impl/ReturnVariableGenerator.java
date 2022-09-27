package cn.bugstack.guide.idea.plugin.service.impl;

import cn.bugstack.guide.idea.plugin.config.JavaDocConstant;
import cn.bugstack.guide.idea.plugin.config.JavaDocConfigComponent;
import cn.bugstack.guide.idea.plugin.service.VariableGenerator;
import cn.bugstack.guide.idea.plugin.config.JavaDocConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

/**
 * @author <a href="mailto:wenguoxing@126.com">wenguoxing</a>
 * @version 1.0.0
 * @since 2019-12-07 23:18:00
 */
public class ReturnVariableGenerator implements VariableGenerator {

    private JavaDocConfiguration config = ServiceManager.getService(JavaDocConfigComponent.class).getState();

    @Override
    public String generate(PsiElement element) {
        if (!(element instanceof PsiMethod)) {
            return "";
        }
        PsiMethod psiMethod = (PsiMethod)element;
        String returnName = psiMethod.getReturnTypeElement() == null ? "" : psiMethod.getReturnTypeElement().getText();

        if (JavaDocConstant.BASE_TYPE_SET.contains(returnName)) {
            return "@return " + returnName;
        } else if ("void".equalsIgnoreCase(returnName)) {
            return "";
        } else {
            if (config.isCodeMethodReturnType()) {
                return "@return {@code " + returnName + " }";
            } else if (config.isLinkMethodReturnType()) {
                return "@return " + returnName.replaceAll("[^<> ,]+", "{@link $0 }");
            }
            return String.format("@return {@link %s }", returnName);
        }
    }
}
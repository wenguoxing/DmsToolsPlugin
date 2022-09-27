package cn.bugstack.guide.idea.plugin.service.impl;

import cn.bugstack.guide.idea.plugin.service.VariableGenerator;
import com.intellij.psi.PsiElement;

/**
 * @author <a href="mailto:wenguoxing@126.com">wenguoxing</a>
 * @version 1.0.0
 * @since 2019-12-07 23:17:00
 */
public class VersionVariableGenerator implements VariableGenerator {

    @Override
    public String generate(PsiElement element) {
        return "1.0.0";
    }
}

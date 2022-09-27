package cn.bugstack.guide.idea.plugin.variable.impl;

import com.intellij.psi.PsiElement;
import cn.bugstack.guide.idea.plugin.variable.VariableGenerator;

/**
 * @author <a href="mailto:wenguoxing.star@gmail.com">wenguoxing</a>
 * @version 1.0.0
 * @since 2019-12-07 23:19:00
 */
public class SinceVariableGenerator implements VariableGenerator {

    @Override
    public String generate(PsiElement element) {
        return "1.0.0";
    }
}
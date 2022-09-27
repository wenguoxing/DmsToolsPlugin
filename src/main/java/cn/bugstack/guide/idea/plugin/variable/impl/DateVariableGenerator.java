package cn.bugstack.guide.idea.plugin.variable.impl;

import cn.bugstack.guide.idea.plugin.config.JavaDocConfigComponent;
import cn.bugstack.guide.idea.plugin.variable.VariableGenerator;
import cn.bugstack.guide.idea.plugin.ui.JavaDocConfiguration;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="mailto:wenguoxing.star@gmail.com">wenguoxing</a>
 * @version 1.0.0
 * @since 2019-12-07 23:15:00
 */
public class DateVariableGenerator implements VariableGenerator {
    private static final Logger LOGGER = Logger.getInstance(DateVariableGenerator.class);
    private JavaDocConfiguration config = ServiceManager.getService(JavaDocConfigComponent.class).getState();

    @Override
    public String generate(PsiElement element) {
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(config.getDateFormat()));
        } catch (Exception e) {
            LOGGER.error("您输入的日期格式不正确，请到配置中修改类相关日期格式！", e);
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern(JavaDocConfigComponent.DEFAULT_DATE_FORMAT));
        }
    }
}
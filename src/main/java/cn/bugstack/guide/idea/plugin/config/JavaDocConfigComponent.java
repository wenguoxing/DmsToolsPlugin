package cn.bugstack.guide.idea.plugin.config;

import cn.bugstack.guide.idea.plugin.ui.JavaDocConfiguration;
import cn.bugstack.guide.idea.plugin.ui.JavaDocConfiguration.TemplateConfig;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 9:19
 * @Version 1.0
 */
@State(name = "easyJavadoc", storages = {@Storage("easyJavadoc.xml")})
public class JavaDocConfigComponent implements PersistentStateComponent<JavaDocConfiguration> {

    public static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    private JavaDocConfiguration configuration;

    /**
     * 构造函数
     */
    @Nullable
    @Override
    public JavaDocConfiguration getState() {
        if (configuration == null) {
            configuration = new JavaDocConfiguration();
            configuration.setAuthor(System.getProperty("user.name"));
            configuration.setDateFormat(DEFAULT_DATE_FORMAT);
            configuration.setSimpleFieldDoc(true);
            configuration.setMethodReturnType(JavaDocConfiguration.LINK_RETURN_TYPE);
            configuration.setWordMap(new TreeMap<>());
            configuration.setTranslator(Consts.YOUDAO_TRANSLATOR);
            configuration.setClassTemplateConfig(new TemplateConfig());
            configuration.setMethodTemplateConfig(new TemplateConfig());
            configuration.setFieldTemplateConfig(new TemplateConfig());
        }
        return configuration;
    }

    @Override
    public void loadState(@NotNull JavaDocConfiguration state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }
}
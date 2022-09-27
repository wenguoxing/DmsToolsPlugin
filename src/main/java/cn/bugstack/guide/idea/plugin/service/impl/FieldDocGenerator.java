package cn.bugstack.guide.idea.plugin.service.impl;

import cn.bugstack.guide.idea.plugin.config.JavaDocConfigComponent;
import cn.bugstack.guide.idea.plugin.service.DocGenerator;
import cn.bugstack.guide.idea.plugin.service.VariableGeneratorService;
import cn.bugstack.guide.idea.plugin.config.JavaDocConfiguration;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.javadoc.PsiDocComment;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 10:55
 * @Version 1.0
 */
public class FieldDocGenerator implements DocGenerator {

    private JavaDocConfiguration config = ServiceManager.getService(JavaDocConfigComponent.class).getState();
    private VariableGeneratorService variableGeneratorService = ServiceManager.getService(VariableGeneratorService.class);

    /**
     * 构造函数
     */
    public FieldDocGenerator() {
    }

    @Override
    public String generate(PsiElement psiElement) {
        if (!(psiElement instanceof PsiField)) {
            return StringUtils.EMPTY;
        }
        PsiField psiField = (PsiField)psiElement;
        if (config != null && config.getFieldTemplateConfig() != null
                && Boolean.TRUE.equals(config.getFieldTemplateConfig().getIsDefault())) {
            return defaultGenerate(psiField);
        } else {
            return customGenerate(psiField);
        }
    }

    /**
     * 默认的生成
     *
     * @param psiField 当前属性
     * @return {@link java.lang.String}
     */
    private String defaultGenerate(PsiField psiField) {
        if (BooleanUtils.isTrue(config.getSimpleFieldDoc())) {
            return genSimpleDoc(psiField.getName());
        } else {
            return genNormalDoc(psiField, psiField.getName());
        }
    }

    /**
     * 自定义生成
     *
     * @param psiField 当前属性
     * @return {@link String}
     */
    private String customGenerate(PsiField psiField) {
        return variableGeneratorService.generate(psiField);
    }

    /**
     * 生成正常的文档
     *
     * @param psiField 属性
     * @param name 名字
     * @return {@link java.lang.String}
     */
    private String genNormalDoc(PsiField psiField, String name) {
        PsiDocComment comment = psiField.getDocComment();
        if (comment != null) {
            List<PsiElement> elements = Lists.newArrayList(comment.getChildren());

            // 注释
            //String desc = translatorService.translate(name);
            String desc = name;
            List<String> commentItems = Lists.newLinkedList();
            for (PsiElement element : elements) {
                commentItems.add(element.getText());
            }
            commentItems.add(1, buildDesc(elements, desc));
            return Joiner.on(StringUtils.EMPTY).skipNulls().join(commentItems);
        }
        //return String.format("/**%s* %s%s */%s", "\n", translatorService.translate(name), "\n",
        return String.format("/**%s* %s%s */%s", "\n", name, "\n", "\n");
    }

    /**
     * 构建描述
     *
     * @param elements 元素
     * @param desc 描述
     * @return {@link java.lang.String}
     */
    private String buildDesc(List<PsiElement> elements, String desc) {
        for (PsiElement element : elements) {
            if (!"PsiDocToken:DOC_COMMENT_DATA".equalsIgnoreCase(element.toString())) {
                continue;
            }
            String source = element.getText().replaceAll("[/* \n]+", StringUtils.EMPTY);
            if (Objects.equals(source, desc)) {
                return null;
            }
        }
        return desc;
    }

    /**
     * 生成简单的文档
     *
     * @param name 的名字
     * @return {@link java.lang.String}
     */
    private String genSimpleDoc(String name) {
        //return String.format("/** %s */", translatorService.translate(name));
        return String.format("/** %s */", name);
    }
}
package cn.bugstack.guide.idea.plugin.service;

import cn.bugstack.guide.idea.plugin.service.impl.ClassDocGenerator;
import cn.bugstack.guide.idea.plugin.service.impl.FieldDocGenerator;
import cn.bugstack.guide.idea.plugin.service.impl.MethodDocGenerator;
import cn.bugstack.guide.idea.plugin.service.impl.PackageInfoDocGenerator;
import com.google.common.collect.ImmutableMap;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 11:03
 * @Version 1.0
 */
public class DocGeneratorService {

    /** docGeneratorMap */
    private Map<Class<? extends PsiElement>, DocGenerator> docGeneratorMap
            = ImmutableMap.<Class<? extends PsiElement>, DocGenerator>builder()
            .put(PsiClass.class, new ClassDocGenerator())
            .put(PsiMethod.class, new MethodDocGenerator())
            .put(PsiField.class, new FieldDocGenerator())
            .put(PsiPackage.class, new PackageInfoDocGenerator())
            .build();

    /**
     * generate
     *
     * @param psiElement psiElement
     * @return {@link String}
     */
    public String generate(PsiElement psiElement) {
        DocGenerator docGenerator = null;
        for (Entry<Class<? extends PsiElement>, DocGenerator> entry : docGeneratorMap.entrySet()) {
            //psiElement根据定位的情况，可能是PsiClass、PsiMethod、PsiField、PsiPackage等。
            if (entry.getKey().isAssignableFrom(psiElement.getClass())) {
                docGenerator = entry.getValue();
                break;
            }
        }
        if (Objects.isNull(docGenerator)) {
            return StringUtils.EMPTY;
        }

        //根据PsiElement的类型，生成相应的注释
        return docGenerator.generate(psiElement);
    }

    /**
     * 构造函数
     */
    public DocGeneratorService() {
    }
}
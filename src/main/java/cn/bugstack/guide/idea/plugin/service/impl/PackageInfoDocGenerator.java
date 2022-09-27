package cn.bugstack.guide.idea.plugin.service.impl;

import cn.bugstack.guide.idea.plugin.action.PackageInfoHandle;
import cn.bugstack.guide.idea.plugin.service.DocGenerator;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 10:59
 * @Version 1.0
 */
public class PackageInfoDocGenerator implements DocGenerator {

    /**
     * 构造函数
     */
    public PackageInfoDocGenerator() {
    }

    @Override
    public String generate(PsiElement psiElement) {
        if (!(psiElement instanceof PsiPackage)) {
            return StringUtils.EMPTY;
        }
        PsiPackage psiPackage = (PsiPackage)psiElement;

        return defaultGenerate(psiPackage);
    }

    private String defaultGenerate(PsiPackage psiPackage) {
        return "/**\n" +
                " * ${" + PackageInfoHandle.PACKAGE_INFO_DESCRIBE + "} \n" +
                "**/\n";
    }
}
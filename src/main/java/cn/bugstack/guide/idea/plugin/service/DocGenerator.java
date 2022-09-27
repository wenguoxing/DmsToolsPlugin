package cn.bugstack.guide.idea.plugin.service;

import com.intellij.psi.PsiElement;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/26 14:56
 * @Version 1.0
 */
public interface DocGenerator {
    /**
     * 生成
     *
     * @param psiElement psiElement
     * @return java.lang.String
     */
    String generate(PsiElement psiElement);
}
package cn.bugstack.guide.idea.plugin.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 9:01
 * @Version 1.0
 */
public class JavaDocConfiguration {

    /**
     * 构造函数
     */
    public JavaDocConfiguration() {
    }


    /** 方法返回值为code类型 */
    public static final String CODE_RETURN_TYPE = "code";
    /** 方法返回值为link类型 */
    public static final String LINK_RETURN_TYPE = "link";

    /**
     * 作者
     */
    private String author = "admin";
    /**
     * 日期格式
     */
    private String dateFormat = "yyyy/MM/dd";
    /**
     * 属性是否使用简单模式
     */
    private Boolean simpleFieldDoc = false;
    /**
     * 属性是否使用简单模式
     */
    private String methodReturnType = LINK_RETURN_TYPE;
    /**
     * 翻译方式
     */
    private String translator = "有道翻译";
    /**
     * 百度app id
     */
    private String appId;
    /**
     * 百度密钥
     */
    private String token;
    /**
     * 腾讯secretKey
     */
    private String secretKey;
    /**
     * 腾讯secretId
     */
    private String secretId;
    /**
     * 阿里云accessKeyId
     */
    private String accessKeyId;
    /**
     * 阿里云accessKeySecret
     */
    private String accessKeySecret;
    /**
     * 单词映射
     */
    private SortedMap<String, String> wordMap = new TreeMap<>();

    /**
     * 类模板配置
     */
    private TemplateConfig classTemplateConfig = new TemplateConfig();
    /**
     * 方法模板配置
     */
    private TemplateConfig methodTemplateConfig = new TemplateConfig();
    /**
     * 属性模板配置
     */
    private TemplateConfig fieldTemplateConfig = new TemplateConfig();

    /** 批量生成是否生成类注释 */
    private Boolean genAllClass;
    /** 批量生成是否生成方法注释 */
    private Boolean genAllMethod;
    /** 批量生成是否生成属性注释 */
    private Boolean genAllField;
    /** 批量生成是否递归内部类 */
    private Boolean genAllInnerClass;

    public void reset() {
        author = "admin";
        dateFormat = "yyyy/MM/dd";
        simpleFieldDoc = false;
        translator = "有道翻译";
        appId = null;
        token = null;
        secretKey = null;
        secretId = null;
        accessKeyId = null;
        accessKeySecret = null;
        wordMap = new TreeMap<>();
        classTemplateConfig = new TemplateConfig();
        methodTemplateConfig = new TemplateConfig();
        fieldTemplateConfig = new TemplateConfig();
    }

    /**
     * 模板配置
     */
    public static class TemplateConfig {
        /**
         * 是否默认的
         */
        private Boolean isDefault;
        /**
         * 模板
         */
        private String template;
        /**
         * 自定义映射
         */
        private Map<String, CustomValue> customMap;

        public TemplateConfig() {
            isDefault = true;
            template = "";
            customMap = new TreeMap<>();
        }

        public Boolean getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(Boolean isDefault) {
            this.isDefault = isDefault;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public Map<String, CustomValue> getCustomMap() {
            return customMap;
        }

        public void setCustomMap(Map<String, CustomValue> customMap) {
            this.customMap = customMap;
        }
    }

    /**
     * 自定义值
     */
    public static class CustomValue {
        /**
         * 类型
         */
        private VariableType type;
        /**
         * 值
         */
        private String value;

        public CustomValue() {
        }

        public CustomValue(VariableType type, String value) {
            this.type = type;
            this.value = value;
        }

        public VariableType getType() {
            return type;
        }

        public void setType(VariableType type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 变量类型
     */
    public static enum VariableType {
        /**
         * 固定值
         */
        STRING("固定值"),
        /**
         * groovy脚本
         */
        GROOVY("Groovy脚本");

        private final String desc;

        VariableType(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        public static VariableType fromDesc(String desc) {
            for (VariableType value : values()) {
                if (value.desc.equals(desc)) {
                    return value;
                }
            }
            return null;
        }
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public Boolean getSimpleFieldDoc() {
        return simpleFieldDoc;
    }

    public void setSimpleFieldDoc(Boolean simpleFieldDoc) {
        this.simpleFieldDoc = simpleFieldDoc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public SortedMap<String, String> getWordMap() {
        if (wordMap == null) {
            wordMap = new TreeMap<>();
        }
        return wordMap;
    }

    public void setWordMap(SortedMap<String, String> wordMap) {
        this.wordMap = wordMap;
    }

    public TemplateConfig getClassTemplateConfig() {
        if (classTemplateConfig == null) {
            classTemplateConfig = new TemplateConfig();
        }
        return classTemplateConfig;
    }

    public void setClassTemplateConfig(TemplateConfig classTemplateConfig) {
        this.classTemplateConfig = classTemplateConfig;
    }

    public TemplateConfig getMethodTemplateConfig() {
        if (methodTemplateConfig == null) {
            methodTemplateConfig = new TemplateConfig();
        }
        return methodTemplateConfig;
    }

    public void setMethodTemplateConfig(TemplateConfig methodTemplateConfig) {
        this.methodTemplateConfig = methodTemplateConfig;
    }

    public TemplateConfig getFieldTemplateConfig() {
        if (fieldTemplateConfig == null) {
            fieldTemplateConfig = new TemplateConfig();
        }
        return fieldTemplateConfig;
    }

    public void setFieldTemplateConfig(TemplateConfig fieldTemplateConfig) {
        this.fieldTemplateConfig = fieldTemplateConfig;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getMethodReturnType() {
        return methodReturnType;
    }

    public void setMethodReturnType(String methodReturnType) {
        this.methodReturnType = methodReturnType;
    }

    @JsonIgnore
    public boolean isCodeMethodReturnType() {
        return CODE_RETURN_TYPE.equals(methodReturnType);
    }

    @JsonIgnore
    public boolean isLinkMethodReturnType() {
        return LINK_RETURN_TYPE.equals(methodReturnType);
    }

    public Boolean getGenAllClass() {
        return genAllClass;
    }

    public void setGenAllClass(Boolean genAllClass) {
        this.genAllClass = genAllClass;
    }

    public Boolean getGenAllMethod() {
        return genAllMethod;
    }

    public void setGenAllMethod(Boolean genAllMethod) {
        this.genAllMethod = genAllMethod;
    }

    public Boolean getGenAllField() {
        return genAllField;
    }

    public void setGenAllField(Boolean genAllField) {
        this.genAllField = genAllField;
    }

    public Boolean getGenAllInnerClass() {
        return genAllInnerClass;
    }

    public void setGenAllInnerClass(Boolean genAllInnerClass) {
        this.genAllInnerClass = genAllInnerClass;
    }
}
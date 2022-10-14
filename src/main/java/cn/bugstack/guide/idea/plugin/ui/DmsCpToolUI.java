package cn.bugstack.guide.idea.plugin.ui;

import cn.bugstack.guide.idea.plugin.config.DmsToolsConstant;
import cn.bugstack.guide.idea.plugin.infrastructure.utils.FileUtil;
import cn.bugstack.guide.idea.plugin.infrastructure.utils.MyBundle;
import cn.bugstack.guide.idea.plugin.module.FileChooserComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.List;

/**
 * DmsCpToolUI
 *
 * @author Administrator
 * @date 2022/10/14
 */
public class DmsCpToolUI extends DialogWrapper {
    private JPanel contentPane;
    private JList listShow;
    private JTextField textFieldProject;
    private JTextField textFieldSvn;
    private JLabel labProject;
    private JLabel labSvn;
    private JPanel Settings;
    private JLabel labFile;
    private JTextField textFieldIniFile;
    private JButton btnSelect;
    private JButton btnFileSelect;
    private JComboBox comboBoxSvn;
    private JLabel labAllSvn;
    private JRadioButton radioButtonCdist;
    private JRadioButton radioButtonJava;
    private JRadioButton radioButtonTact;
    private JRadioButton radioButtonAplBase;

    private DefaultListModel listModel;

    /** 自定义Action */
    private CustomOKAction okAction;
    private DialogWrapperExitAction exitAction;

    private Project project;
    private PsiFile psiFile;
    private VirtualFile virtualFile;

    private int projectType = 0;

    /** apppath */
    private String APPPATH = "";

    /** WEBPATH */
    private String WEBPATH = "";

    /** DBBATCHPATH */
    private String DBBATCHPATH = "";

    /** APBATCHPATH */
    private String APBATCHPATH = "";

    /** RESOURCEPATH */
    private String RESOURCEPATH = "";

    public DmsCpToolUI() {
        super(true);
        init();
    }

    public DmsCpToolUI(@Nullable Project project) {
        super(true);
        init();
    }

    public DmsCpToolUI(@Nullable Project project, @Nullable PsiFile psiFile, int projectType) {
        super(true);
        init();
        this.project = project;
        this.psiFile = psiFile;
        this.projectType = projectType;
        initOther();
    }

    public DmsCpToolUI(@Nullable Project project, @Nullable PsiFile psiFile, @Nullable VirtualFile virtualFile, int projectType) {
        super(true);
        init();
        this.project = project;
        this.psiFile = psiFile;
        this.projectType = projectType;
        this.virtualFile = virtualFile;
        initOther();
    }

    /**
     * initOther
     */
    public void initOther(){

        //初始标题
        this.setTitleContext();

        //读取资源
        MyBundle myBundle = MyBundle.getInstance();
        //找到配置文件
        String iniFilePath = myBundle.getValue("INIFILEPATH");
        if (null != iniFilePath && !"".equals(iniFilePath)) {
            this.textFieldIniFile.setText(iniFilePath);
            //读取配置文件
            List<String> datas = FileUtil.queryData(iniFilePath);
            this.listShow.setListData(datas.toArray());
            this.textFieldSvn.setText(datas.get(projectType));

            //去重后,填充下拉框
            datas.stream().distinct().forEach(p -> this.comboBoxSvn.addItem(p));
        }

        //显示项目
        this.textFieldProject.setText(project.getBasePath());
        this.listShow.setListData(
                new String[]{project.toString(),
                        project.getProjectFilePath(),
                        psiFile.getVirtualFile().getPath()});

        //SVN选择
        this.btnSelect.addActionListener(e -> {
            //自定义选择框
            FileChooserComponent component = FileChooserComponent.getInstance(project);
            VirtualFile baseDir = project.getBaseDir();
            VirtualFile virtualFile = component.showFolderSelectionDialog("选择SVN目录", baseDir, baseDir);
            if (null != virtualFile) {
                this.textFieldSvn.setText(virtualFile.getPath());
            }
        });

        //ini文件选择
        this.btnFileSelect.addActionListener(e -> {
            //自定义选择框
            FileChooserComponent component = FileChooserComponent.getInstance(project);
            VirtualFile baseDir = project.getBaseDir();
            VirtualFile virtualFile = component.showFileSelectionDialog("选择配置文件", baseDir, baseDir);
            if(virtualFile != null) {
                //Messages.showMessageDialog(virtualFile.getName(), "获取到的文件名称", Messages.getInformationIcon());
                String fileName = virtualFile.getPath();
                this.textFieldIniFile.setText(fileName);
                List<String> datas = FileUtil.queryData(fileName);
                this.listShow.setListData(datas.toArray());
                this.textFieldSvn.setText(datas.get(projectType));

                //去重填充下拉框
                datas.stream().distinct().forEach(p -> this.comboBoxSvn.addItem(p));

            } else {
                //Messages.showMessageDialog("文件名称为空", "文件名称为空", Messages.getInformationIcon());
            }
        });

        // 添加条目选中状态改变的监听器
        comboBoxSvn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // 只处理选中的状态
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    textFieldSvn.setText(comboBoxSvn.getSelectedItem().toString());
                }
            }
        });

        // 设置默认选中的条目
        //comboBoxSvn.setSelectedIndex(0);

        //单选框组
        ButtonGroup btnGroupSys = new ButtonGroup();
        btnGroupSys.add(radioButtonCdist);
        btnGroupSys.add(radioButtonTact);
        radioButtonCdist.setSelected(true);

        ButtonGroup btnGroupSubSys = new ButtonGroup();
        btnGroupSubSys.add(radioButtonJava);
        btnGroupSubSys.add(radioButtonAplBase);
        radioButtonJava.setSelected(true);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        setModal(true);
        return contentPane;
    }

    /**
     * getRadioSelectText
     *
     * @param radioButtons radioButtons
     * @return {@link String}
     */
    protected void getRadioSelectText(List<JRadioButton> radioButtons) {
        radioButtons.stream().forEach(element->{
            if(element.getModel().isSelected()){
                this.textFieldSvn.setText(element.getText());
            }
        });
    }

    /**
     * doCopy
     */
    public void doCopyJava(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(DmsToolsConstant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + DmsToolsConstant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + this.APPPATH + "/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;

            //日志
            this.listShow.setListData(new String[]{srcFileFullPath,desFileFullPath});

            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * doCopyWeb
     */
    public void doCopyWeb(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(DmsToolsConstant.PROWEBPATH);
            String srcFilePath = srcFileFullPath.substring(idx + DmsToolsConstant.PROWEBPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + this.WEBPATH + "/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;

            //日志
            this.listShow.setListData(new String[]{srcFileFullPath,desFileFullPath});

            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * doCopyDB
     */
    public void doCopyDBBatch(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(DmsToolsConstant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + DmsToolsConstant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + this.DBBATCHPATH + "/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;

            //日志
            this.listShow.setListData(new String[]{srcFileFullPath,desFileFullPath});

            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * doCopyAPBatch
     */
    public void doCopyAPBatch(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(DmsToolsConstant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + DmsToolsConstant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            //获取当前Module
            ProjectFileIndex fileIndex = ProjectFileIndex.getInstance(project);
            String desPath = this.textFieldSvn.getText() + "/" + this.APBATCHPATH + "/" + fileIndex.getModuleForFile(virtualFile).getName() + "/" + "src/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;

            //日志
            this.listShow.setListData(new String[]{srcFileFullPath,desFileFullPath});

            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * doCopyResource
     */
    public void doCopyResource(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(DmsToolsConstant.PRORESOURCEPATH);
            String srcFilePath = srcFileFullPath.substring(idx + DmsToolsConstant.PRORESOURCEPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + this.RESOURCEPATH + "/" + srcPath;
            String desFileFullPath = desPath + "/" + srcFileName;

            //日志
            this.listShow.setListData(new String[]{srcFileFullPath,desFileFullPath});

            int iRet = FileUtil.makeUploadDir(desPath);
            if (iRet == 0) {
                iRet = FileUtil.copyFile(srcFileFullPath, desFileFullPath);
                if (iRet == 0) {
                    Messages.showMessageDialog(srcFileName + "复制成功","复制成功", Messages.getInformationIcon());
                    //close(CANCEL_EXIT_CODE);
                }
            } else {
                Messages.showMessageDialog("创建路径出错!","复制出错", Messages.getInformationIcon());
            }
        } catch (IOException e) {
            Messages.showMessageDialog(e.getMessage(),"复制出错", Messages.getInformationIcon());
        }
    }

    /**
     * getAppPath
     *
     * @return {@link String}
     */
    private void getAppPath() {
        boolean bolCdist = radioButtonCdist.isSelected();
        boolean bolTact = radioButtonTact.isSelected();
        boolean bolJava = radioButtonJava.isSelected();
        boolean bolAplBase = radioButtonAplBase.isSelected();

        if (bolCdist && bolJava) {
            this.APPPATH = DmsToolsConstant.APPATH_CDIST;
        } else if (bolCdist && bolAplBase) {
            this.APPPATH = DmsToolsConstant.APLBASEPATH_CDIST;
        } else if (bolTact && bolJava) {
            this.APPPATH = DmsToolsConstant.APPATH_TACT;
        } else if (bolTact && bolAplBase) {
            this.APPPATH = DmsToolsConstant.APLBASEPATH_TACT;
        }
    }

    /**
     * getOtherPath
     *
     * @return {@link String}
     */
    private void getOtherPath() {
        boolean bolCdist = radioButtonCdist.isSelected();
        boolean bolTact = radioButtonTact.isSelected();

        if (bolCdist) {
            this.WEBPATH = DmsToolsConstant.WEBPATH_CDIST;
            this.DBBATCHPATH = DmsToolsConstant.DBBATCHPATH_CDIST;
            this.APBATCHPATH = DmsToolsConstant.APBATCHPATH_CDIST;
            this.RESOURCEPATH = DmsToolsConstant.RESOURCEPATH_CDIST;
        } else if (bolTact) {
            this.WEBPATH = DmsToolsConstant.WEBPATH_TACT;
            this.DBBATCHPATH = DmsToolsConstant.DBBATCHPATH_TACT;
            this.APBATCHPATH = DmsToolsConstant.APBATCHPATH_TACT;
            this.RESOURCEPATH = DmsToolsConstant.RESOURCEPATH_TACT;
        }
    }

    /**
     * setTitleContext
     */
    protected void setTitleContext() {
        String title = "";
        switch (projectType) {
            case 0:
                title = "IZJava拷贝";
                break;
            case 1:
                title = "IZWeb拷贝";
                break;
            case 2:
                title = "DB-Batch拷贝";
                break;
            case 3:
                title = "Ap-Batch拷贝";
                break;
            case 4:
                title = "Resource拷贝";
                break;
        }

        setTitle(title);
    }

    /**
     * 覆盖默认的ok/cancel按钮
     *
     * @return
     */
    @NotNull
    @Override
    protected Action[] createActions() {
        //定义取消按键
        exitAction = new DialogWrapperExitAction("取消", CANCEL_EXIT_CODE);
        //定义确定按钮
        okAction = new CustomOKAction();
        // 设置默认的焦点按钮
        okAction.putValue(DialogWrapper.DEFAULT_ACTION, true);
        return new Action[]{okAction,exitAction};
    }

    /**
     * 自定义 ok Action
     */
    protected class CustomOKAction extends DialogWrapperAction {

        protected CustomOKAction() {
            super("确定");
        }

        @Override
        protected void doAction(ActionEvent e) {

            //获取app路径
            getAppPath();
            //获取其它路径
            getOtherPath();

            switch (projectType) {
                case 0:
                    doCopyJava();
                    break;
                case 1:
                    doCopyWeb();
                    break;
                case 2:
                    doCopyDBBatch();
                    break;
                case 3:
                    doCopyAPBatch();
                    break;
                case 4:
                    doCopyResource();
                    break;
            }

            close(CANCEL_EXIT_CODE);
        }
    }
}

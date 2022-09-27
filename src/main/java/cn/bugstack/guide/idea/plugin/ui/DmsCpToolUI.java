package cn.bugstack.guide.idea.plugin.ui;

import cn.bugstack.guide.idea.plugin.infrastructure.utils.FileUtil;
import cn.bugstack.guide.idea.plugin.infrastructure.utils.MyBundle;
import cn.bugstack.guide.idea.plugin.module.FileChooserComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

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

    private DefaultListModel listModel;

    private CustomOKAction okAction;
    private DialogWrapperExitAction exitAction;

    private Project project;
    private PsiFile psiFile;
    private VirtualFile virtualFile;

    private int projectType = 0;

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

        this.setTitleContext();

        MyBundle myBundle = MyBundle.getInstance();
        String iniFilePath = myBundle.getValue("INIFILEPATH");
        if (null != iniFilePath && !"".equals(iniFilePath)) {
            this.textFieldIniFile.setText(iniFilePath);
            List<String> datas = FileUtil.queryData(iniFilePath);
            this.listShow.setListData(datas.toArray());
            this.textFieldSvn.setText(datas.get(projectType));
        }

        this.textFieldProject.setText(project.getBasePath());
        this.listShow.setListData(
                new String[]{project.toString(),
                        project.getProjectFilePath(),
                        psiFile.getVirtualFile().getPath()});

        //SVN选择
        this.btnSelect.addActionListener(e -> {
            FileChooserComponent component = FileChooserComponent.getInstance(project);
            VirtualFile baseDir = project.getBaseDir();
            VirtualFile virtualFile = component.showFolderSelectionDialog("选择SVN目录", baseDir, baseDir);
            if (null != virtualFile) {
                this.textFieldSvn.setText(virtualFile.getPath());
            }
        });

        //ini文件选择
        this.btnFileSelect.addActionListener(e -> {
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
            } else {
                Messages.showMessageDialog("文件名称为空", "文件名称为空", Messages.getInformationIcon());
            }
        });
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        setModal(true);
        return contentPane;
    }

    /**
     * 校验数据
     * @return 通过必须返回null，不通过返回一个 ValidationInfo 信息
     */
    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        //String text = inputTextField.getText();
        //if(StringUtils.isNotBlank(text)) {
        //    return null;
        //} else {
        //    return new ValidationInfo("校验不通过");
        //}

        return null;
    }

    /**
     * doCopy
     */
    public void doCopy(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(Constant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + Constant.APPATH + "/" + srcPath;
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
            int idx = srcFileFullPath.indexOf(Constant.PROWEBPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PROWEBPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + Constant.WEBPATH + "/" + srcPath;
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
    public void doCopyDB(){
        try {
            String srcFileFullPath = psiFile.getVirtualFile().getPath();
            String srcFileName = psiFile.getVirtualFile().getName();

            //去掉当前项目的路径
            int idx = srcFileFullPath.indexOf(Constant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + Constant.DBBATCHPATH + "/" + srcPath;
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
            int idx = srcFileFullPath.indexOf(Constant.PROPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PROPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            ProjectFileIndex fileIndex = ProjectFileIndex.getInstance(project);
            //Messages.showInfoMessage("Module: " + fileIndex.getModuleForFile(file) + "\n" +
            //                "Is In Source: " + fileIndex.isInSource(file) + "\n" +
            //                "Module content root: " + fileIndex.getContentRootForFile(file) + "\n" +
            //                "Source root: " + fileIndex.getSourceRootForFile(file) + "\n" +
            //                "Is library file: " + fileIndex.isLibraryClassFile(file) + "\n" +
            //                "Is in library classes: " + fileIndex.isInLibraryClasses(file) +
            //                ", Is in library source: " + fileIndex.isInLibrarySource(file),
            //        "Main File Info for" + file.getName());
            String desPath = this.textFieldSvn.getText() + "/" + Constant.APBATCHPATH + "/" + fileIndex.getModuleForFile(virtualFile).getName() + "/" + "src/" + srcPath;
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
            int idx = srcFileFullPath.indexOf(Constant.PRORESOURCEPATH);
            String srcFilePath = srcFileFullPath.substring(idx + Constant.PRORESOURCEPATH.length());
            idx = srcFilePath.indexOf(srcFileName);
            String srcPath = srcFilePath.substring(0, idx - 1);

            String desPath = this.textFieldSvn.getText() + "/" + Constant.RESOURCEPATH + "/" + srcPath;
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
     * setTitleContext
     */
    protected void setTitleContext() {
        String title = "";
        switch (projectType) {
            case 0:
                title = "AP拷贝";
                break;
            case 1:
                title = "Web拷贝";
                break;
            case 2:
                title = "Batch拷贝";
                break;
            case 3:
                title = "ApBatch拷贝";
                break;
            case 4:
                title = "Resource拷贝";
                break;
        }

        setTitle(title);
    }

    /**
     * 覆盖默认的ok/cancel按钮
     * @return
     */
    @NotNull
    @Override
    protected Action[] createActions() {
        exitAction = new DialogWrapperExitAction("取消", CANCEL_EXIT_CODE);
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
            switch (projectType) {
                case 0:
                    doCopy();
                    break;
                case 1:
                    doCopyWeb();
                    break;
                case 2:
                    doCopyDB();
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

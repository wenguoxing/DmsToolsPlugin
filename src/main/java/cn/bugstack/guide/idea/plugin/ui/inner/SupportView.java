package cn.bugstack.guide.idea.plugin.ui.inner;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 11:17
 * @Version 1.0
 */
public class SupportView extends DialogWrapper {

    private JLabel alipayLabel;
    private JLabel wechatLabel;
    private JPanel panel;

    public SupportView() {
        super(false);
        init();
        setTitle("感谢大佬！");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return panel;
    }

    private void createUIComponents() {
        ImageIcon alipayIcon = new ImageIcon(this.getClass().getResource("/alipay.jpg"));
        double scale = 400d / alipayIcon.getIconHeight();
        alipayIcon.setImage(alipayIcon.getImage().getScaledInstance((int)(alipayIcon.getIconWidth() * scale), 400, Image.SCALE_DEFAULT));
        alipayLabel = new JLabel(alipayIcon);
        alipayLabel.setVisible(true);

        ImageIcon wechatIcon = new ImageIcon(this.getClass().getResource("/wechat.jpg"));
        scale = 400d / wechatIcon.getIconHeight();
        wechatIcon.setImage(wechatIcon.getImage().getScaledInstance((int)(wechatIcon.getIconWidth() * scale), 400, Image.SCALE_DEFAULT));
        wechatLabel = new JLabel(wechatIcon);
        wechatLabel.setVisible(true);
    }
}

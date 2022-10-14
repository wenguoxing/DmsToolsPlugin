package cn.bugstack.guide.idea.plugin.listener;

import cn.bugstack.guide.idea.plugin.ui.inner.SupportView;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationActivationListener;
import com.intellij.openapi.wm.IdeFrame;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URI;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/27 11:11
 * @Version 1.0
 */
public class AppActivationListener implements ApplicationActivationListener {

    /** 上一次通知时间 */
    private volatile long lastNoticeTime = 0L;
    /** 通知时间间隔 */
    private static final long INTERVAL = 7 * 24 * 60 * 60 * 1000L;

    @Override
    public synchronized void applicationActivated(@NotNull IdeFrame ideFrame) {
        if (System.currentTimeMillis() - lastNoticeTime < INTERVAL) {
            return;
        }

        //生成通知
        Notification notification = NotificationGroupManager.getInstance()
                .getNotificationGroup("CustomNotificationGroup")
                .createNotification("支持该插件","请支持一下开发者！", NotificationType.INFORMATION);

        // 去点star
        notification.addAction(new NotificationAction("✨ 去点star") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                try {
                    Desktop dp = Desktop.getDesktop();
                    if (dp.isSupported(Desktop.Action.BROWSE)) {
                        dp.browse(URI.create("https://github.com/wenguoxing"));
                    }
                } catch (Exception ex) {
                    //LOGGER.error("打开链接失败:https://github.com/starcwang/easy_javadoc", ex);
                }
            }
        });

        // 支付
        notification.addAction(new NotificationAction("☕ 请喝咖啡") {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
                SupportView supportView = new SupportView();
                supportView.show();
            }
        });

        notification.notify(null);
        lastNoticeTime = System.currentTimeMillis();
    }

    @Override
    public void applicationDeactivated(@NotNull IdeFrame ideFrame) {
        applicationActivated(ideFrame);
    }

    /**
     * 构造函数
     */
    public AppActivationListener() {
    }
}
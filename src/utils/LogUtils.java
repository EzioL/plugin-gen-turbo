package utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

/**
 * @creed: Here be dragons !
 * @author: EzioQAQ
 * @Time: 2019-04-15 14:36
 */
public class LogUtils {
    public static void showError(String content) {
        Notifications.Bus.notify(new Notification("Ezio", "Ezio", content, NotificationType.ERROR));
    }

    public static void showInfo(String content) {
        Notifications.Bus.notify(new Notification("Ezio", "Ezio的提示", content, NotificationType.INFORMATION));
    }

}

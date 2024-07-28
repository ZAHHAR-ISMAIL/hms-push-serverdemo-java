import com.huawei.push.examples.SendBatchNotifyMessages;
import com.huawei.push.exception.HuaweiMesssagingException;

public class Main {
    public static void main(String[] args) {

        SendBatchNotifyMessages hmsSend = new SendBatchNotifyMessages();
        try {
            hmsSend.sendNotification();
        } catch (HuaweiMesssagingException e) {
            throw new RuntimeException(e);
        }
    }
}
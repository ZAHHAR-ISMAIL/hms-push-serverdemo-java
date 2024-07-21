import com.huawei.push.SendNotifyMessage;
import com.huawei.push.exception.HuaweiMesssagingException;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // token 2 : IQAAAACy07ONAADq5Az0KdTm0ehBsYczBYtaeADmoHQXkd1TTI6sWahjWFgsN9VAi_YXMsvDR5Qc1WiYRXD_6teci1h-hCGQxtMKmN1_lv_InP2qPg

        SendNotifyMessage hmsSend = new SendNotifyMessage();
        try {
            hmsSend.sendNotification();
        } catch (HuaweiMesssagingException e) {
            throw new RuntimeException(e);
        }
    }
}
import com.muyangren.dubbox.Interface.HelloService;
import com.muyangren.dubbox.Interface.NewTestService;
import com.muyangren.dubbox.Interface.TestService;
import com.muyangren.dubbox.bean.TestResult;
import com.muyangren.dubbox.consumer.ConsumerTool;
import com.muyangren.dubbox.consumer.client.ClientConfig;
import com.muyangren.dubbox.consumer.client.ConsumerClient;
import com.muyangren.dubbox.consumer.constant.ProtocolType;
import com.muyangren.dubbox.exception.NoSuchInterfaceException;

public class Main {

    public static void main(String[] args) {

        ConsumerClient client =ConsumerTool.getConsumerClient("testConsumer", "127.0.0.1", 2181);
        try {
			HelloService hello = client.getProvide(HelloService.class);
            System.out.println(hello.hello());
            NewTestService test = client.getProvide(NewTestService.class);
            System.out.println(test.test());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
}

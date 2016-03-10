package rabbit; /**
 * Created by zql on 16/3/9.
 */

import com.hahazql.tools.msg.mq.IMQClient;
import com.hahazql.tools.msg.mq.IProducer;
import com.hahazql.tools.msg.mq.rabbitmq.RabbitClient;
import com.hahazql.tools.msg.mq.rabbitmq.RabbitMQConsumer;
import com.hahazql.tools.msg.mq.rabbitmq.RabbitMQParam;
import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.IMessageExchange;
import com.hahazql.tools.msg.MessageRecognizer;
import org.junit.Test;

/**
 * Created by zql on 16/3/9.
 * @className RabbitMQTest
 * @classUse
 *
 *
 */
public class RabbitMQTest
{
    RabbitMQParam param = new RabbitMQParam("testChannel");
    @Test
    public void rec()
    {
        try {
            MessageRecognizer recognizer = new MessageRecognizer();
            recognizer.putMessage(new SendMsg());
            IMessageExchange exchange = new IMessageExchange() {
                @Override
                public void recMsg(IMessage message) {
                    if(message instanceof SendMsg)
                    {
                        System.out.println(((SendMsg) message).getMsg());
                    }
                }
            };
            IMQClient client = new RabbitClient("zql","123","172.16.47.153",5672);
            client.bindConsumer(param,new RabbitMQConsumer(exchange),recognizer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void send()
    {
        try {
            IMQClient client = new RabbitClient("zql","123","172.16.47.153",5672);
            IProducer producer = client.createProducer(param);
            producer.sendMsg(new SendMsg("hello"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

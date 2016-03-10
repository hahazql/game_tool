package rabbit;/**
 * Created by zql on 16/3/7.
 */

import com.hahazql.tools.msg.IMessage;
import com.hahazql.tools.msg.IMessageHeader;

import java.io.UnsupportedEncodingException;

/**
 * Created by zql on 16/3/7.
 * @className SendMsg
 * @classUse
 *
 *
 */
public class SendMsg extends IMessage
{
    private String msg;

    public SendMsg()
    {
    }

    public SendMsg(String msg){
        setMsg(msg);
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int getMsgType() {
        return 10;
    }

    @Override
    public IMessage decode(IMessageHeader header, byte[] message) throws UnsupportedEncodingException {
        SendMsg msg = new SendMsg();
        msg.setHeader(header);
        msg.setMsg(new String(message, "UTF-8"));
        return msg;
    }

    @Override
    protected byte[] dataToBytes() {
        return msg.getBytes();
    }
}

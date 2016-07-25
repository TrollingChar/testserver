import io.netty.channel.ChannelHandlerContext;

/**
 * Created by UserName on 06.07.2016.
 */
public class Player {
    String name;
    boolean ready;
    int id;
    ChannelHandlerContext ctx;

    public Player(int id, ChannelHandlerContext ctx)
    {
        this.id = id;
        this.ctx = ctx;
        this.ready = false;
        this.name = name;
    }

    public int sendState(int cmd, String data)
    {
        String result = Base64Codec.Encode(cmd);
        result += data;
        return -1;
    }

    public void sendMessage(String msg) {
        ctx.writeAndFlush(msg+"\n");
    }
}

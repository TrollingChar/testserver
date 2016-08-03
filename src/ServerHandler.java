import java.io.*;

//import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.concurrent.EventExecutor;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //channel.add(ctx.channel()); // Клиент пришел
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //channel.remove(ctx.channel());//Клиент ушел


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String s = (String) msg;

        System.out.println(s);

        int messageType;
        if(s == "") {
            return;
            // пустые сообщения типа пинг и мы не кикаем за них
        } else {
            messageType = Base64Codec.Decode(s.charAt(0));
            s = s.substring(1);
        }

        switch (messageType) {

            case ClientCommands.AUTHORIZE:
                receiveAuthorize(ctx, s);
                break;

            case ClientCommands.TO_BATTLE:
                receiveToBattle(ctx, s);
                break;

            case ClientCommands.CANCEL:
                receiveCancel(ctx, s);
                break;

            case ClientCommands.SYNCHRONIZE:
                receiveSynchronize(ctx, s);
                break;

            case ClientCommands.INPUT_DATA:
                receiveInputData(ctx, s);
                break;

            case ClientCommands.REPEAT:
                receiveRepeat(ctx, s);

            default:
                receiveDisconnect(ctx);
                break;
        }
    }

    private void receiveRepeat(ChannelHandlerContext ctx, String s) {
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        pl.receiveRepeat(Base64Codec.DecodeFromString(new WrapperString(s)));
    }

    private void receiveDisconnect(ChannelHandlerContext ctx) {
        System.out.println("what?");
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        pl.receiveDisconnect();
    }

    private void receiveInputData(ChannelHandlerContext ctx, String s) {
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        pl.receiveInputData(s);
    }

    private void receiveSynchronize(ChannelHandlerContext ctx, String s) {
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        //System.out.println(s.length());
        pl.receiveSynchronize("".equals(s));
    }

    private void receiveCancel(ChannelHandlerContext ctx, String s) {
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        pl.receiveCancel();
    }

    private void receiveToBattle(ChannelHandlerContext ctx, String s) {
        Player pl = Main.I.getPlayer(ctx);
        if(pl == null) ctx.close();
        pl.receiveToBattle();
    }

    private void receiveAuthorize(ChannelHandlerContext ctx, String s) {
        WrapperString ws = new WrapperString(s);
        int id = Base64Codec.DecodeFromString(ws);
        Main.I.receiveAuthorize(ctx, id);
    }
}
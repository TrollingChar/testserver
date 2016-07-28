import io.netty.channel.ChannelHandlerContext;

/**
 * Created by UserName on 06.07.2016.
 */
public class Player {
    String name;
    boolean ready;
    int id;
    ChannelHandlerContext ctx;
    Room room;
    boolean canGetTurn;

    public Player(int id, ChannelHandlerContext ctx)
    {
        this.id = id;
        this.ctx = ctx;
        this.ready = false;
        this.name = name;
    }

    public void sendLoginConfirm() {
        ctx.writeAndFlush("0");
    }

    public void sendCancelBattle() {
        ctx.writeAndFlush("2");
    }

    public void sendStartBattle() {
        String s = "3";
        //
        ctx.writeAndFlush(s);
    }

    public void sendTurn(String s) {

    }

    public void sendActivePlayer(int id) {

    }

    public int receiveReadyToBattle() {
        System.out.println("player " + id + " wants to play!");
        if(ready) return -1;
        System.out.println("adding him to hub");
        ready = true;
        Global.instance.hub.add(this);
        Global.instance.checkHub();
        return 0;
    }

    public int receiveCancelBattle() {
        System.out.println("player " + id + " hit cancel button");
        if(!ready) return -1;
        System.out.println("removing him from hub");
        ready = false;
        Global.instance.hub.remove(this);
        sendCancelBattle();
        return 0;
    }

    public int receiveTurn(String s) {
        if(room == null) return -1;
        room.
    }

    public int receiveSyncronize(boolean alive) {
        if(room == null) return -1;
        room.setReady(this, alive);
    }
    /*
    public int sendState(int cmd, String data)
    {
        String result = Base64Codec.Encode(cmd);
        result += data;
        return -1;
    }

    public void sendMessage(String msg) {
        ctx.writeAndFlush(msg+"\n");
    }
    */
}

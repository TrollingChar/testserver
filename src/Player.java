import io.netty.channel.ChannelHandlerContext;

import java.util.LinkedList;

/**
 * Created by UserName on 06.07.2016.
 */
public class Player {
    public ChannelHandlerContext ctx;
    public int id;
    public boolean inHub;

    public Room room;

    public Player(ChannelHandlerContext ctx, int id) {
        this.ctx = ctx;
        this.id = id;
    }

    public void receiveToBattle() {
        System.out.println("id" + id + " << start");
        // мы не хотим, чтобы игрока кинуло в 2 игры сразу
        if(room != null) return;

        if(inHub) return; // ты уже ждешь боя

        Main.I.receiveToBattle(this);
    }

    public void receiveCancel() {
        System.out.println("id" + id + " << cancel");
        // поздно! мы уже кинули тебя в игру!
        if(room != null) return;

        if(!inHub) return; // нечего отменять

        Main.I.receiveCancel(this);
    }

    public void receiveDisconnect() {
        System.out.println("id" + id + " disconnects");
        if(room != null) {
            room.receiveDisconnect(this);
        }
        Main.I.receiveDisconnect(this);
    }

    public void receiveSynchronize(boolean alive) {
        System.out.println("id" + id + " << syncro" + (alive ? " (alive)" : ""));
        if(room == null) {
            return;
        }
        room.receiveSynchronize(this, alive);
    }

    public void receiveInputData(String s) {
        System.out.println("id" + id + " << ...");
        if(room == null) {
            return;
        }
        room.receiveInputData(this, s);
    }

    public void receiveRepeat(int msgId) {
        if(room == null) {
            return;
        }
        room.receiveRepeat(this, msgId);
    }

    public void sendAuthConfirm() {
        System.out.println(id);
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.AUTH_CONFIRM) + "\n");
        System.out.println("id" + id + " >> connects");
    }

    public void sendStartBattle(int seed, LinkedList<Player> players) {
        String s = "" + Base64Codec.EncodeToChar(players.size());
        for (Player player : players) {
            s += Base64Codec.Encode(player.id);
        }
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.START_BATTLE) + s + Base64Codec.Encode(seed) + "\n");
        System.out.println("id" + id + " >> start, seed = " + seed);
    }

    public void sendCancel() {
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.CANCEL) + "\n");
        System.out.println("id" + id + " >> cancel");
    }

    public void sendHisTurn(int id) {
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.HIS_TURN) + Base64Codec.Encode(id) + "\n");
        System.out.println("id" + this.id + " >> " + id + "'s turn");
    }

    public void sendInputData(int msgId, String s) {
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.INPUT_DATA) + s + "\n");
        System.out.println("id" + id + " >> " + msgId);
    }

    public void sendEndBattle(int winner) {
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.END_BATTLE)+Base64Codec.Encode(winner) + "\n");
        System.out.println("id" + id + " >> " + id + " wins");
    }

    public void sendPlayerLeft(int id) {
        ctx.writeAndFlush(Base64Codec.EncodeToChar(ServerCommands.PLAYER_LEFT)+Base64Codec.Encode(id) + "\n");
        System.out.println("id" + this.id + " >> " + id + " surrenders");
    }
}

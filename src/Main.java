import io.netty.channel.ChannelHandlerContext;

import java.util.*;

/**
 * Created by Дима on 28.07.2016.
 */
public class Main {
    public static Main I;

    Set<Integer> playersId;
    Map<ChannelHandlerContext, Player> players;
    Queue<Player> hub;

    public static void main(String[] args) throws Exception {
        I = new Main();
        Server.configure();
    }

    public Main() throws Exception {
        playersId = new HashSet<>();
        players = new HashMap<>();
        hub = new LinkedList<>();
    }

    public void receiveAuthorize(ChannelHandlerContext ctx, int id) {
        if(!playersId.add(id)) {
            // этот id используется
            ctx.close();
            return;
        }
        if(players.containsKey(ctx)) {
            // ты уже авторизован
            return;
        }
        Player player = new Player(ctx, id);
        players.put(ctx, player);
        player.sendAuthConfirm();
    }

    public void receiveDisconnect(Player player) {
        playersId.remove(player.id);
        players.remove(player.ctx);
        player.ctx.close();
    }

    public Player getPlayer(ChannelHandlerContext ctx) {
        return players.get(ctx);
    }

    public void receiveToBattle(Player player) {
        player.inHub = true;
        hub.add(player);
        while (hub.size() >= 2) {
            Room room = new Room(Arrays.asList(hub.remove(), hub.remove()));
        }
    }

    public void receiveCancel(Player player) {
        hub.remove(player);
        player.inHub = false;
        player.sendCancel();
    }
}

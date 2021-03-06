import java.util.*;

/**
 * Created by UserName on 12.07.2016.
 */
public class Room {
    HashMap<Integer,String> buffer;
    int next;
    LinkedList<Player> players;
    Set<Player> readyPlayers;

    Player activePlayer;
    LinkedList<Player> playerQueue;

    public Room(Collection<Player> players) {
        // это 2 разных списка, у них разные указатели
        buffer = new HashMap<>();
        this.players = new LinkedList<>(players);
        playerQueue = new LinkedList<>(players);
        readyPlayers = new HashSet<>();
        for (Player player : players) {
            player.inHub = false;
            player.room = this;
        }
        sendStartBattle();
    }

    public void receiveDisconnect(Player player) {
        // не рассылаем ему сообщения
        player.room = null;
        players.remove(player);
        playerQueue.remove(player);
        readyPlayers.remove(player);
        if(activePlayer == player) {
            activePlayer = null;
            sendPlayerLeft(player.id);
        }
        if(players.size() == readyPlayers.size()) {
            newTurn();
        }
    }

    private void newTurn() {
        System.out.println("  room: new turn");
        buffer.clear();
        next = 0;
        readyPlayers.clear();
        if(playerQueue.size() > 0) {
            activePlayer = playerQueue.remove();
        } else {
            activePlayer = null;
        }
        if (playerQueue.size() == 0) {
            sendEndBattle();
            for (Player player : players) {
                player.room = null;
            }
        } else {
            playerQueue.add(activePlayer);
            sendHisTurn(activePlayer.id);
        }
    }

    private void sendPlayerLeft(int id) {
        for (Player player : players) {
            player.sendPlayerLeft(id);
        }
    }

    public void receiveSynchronize(Player player, boolean alive) {
        String s = "  room: sync received\n";
        s += "   you: " + player.id + "\n ready:\n";
        for (Player p : readyPlayers) {
            s += "        " + p.id + "\n";
        }
        s += "others:\n";
        for (Player p : players) {
            s += "        " + p.id + "\n";
        }
        System.out.println(s);

        readyPlayers.add(player);
        if (!alive) {
            playerQueue.remove(player);
        }
        if(players.size() == readyPlayers.size()) {
            newTurn();
        }
    }

    public void receiveInputData(Player player, String s) {
        if(player == activePlayer && !readyPlayers.contains(player)) {
            sendInputData(next, s);
            buffer.put(next++, s);
        }
    }

    public void receiveRepeat(Player player, int msgId) {
        if(player != activePlayer) {
            String s = buffer.get(msgId);
            if(s != null) sendInputData(player, msgId, buffer.get(msgId));
        }
    }

    public void sendStartBattle() {
        int seed = (int)(Math.random() * 2147483648.0);
        for (Player player : players) {
            player.sendStartBattle(seed, players);
        }
    }

    public void sendHisTurn(int id) {
        for (Player player : players) {
            player.sendHisTurn(id);
        }
    }

    public void sendInputData(int msgId, String s) {
        for (Player player : players) {
            if(player == activePlayer) continue;
            sendInputData(player, msgId, s);
        }
    }

    public void sendInputData(Player player, int msgId, String s) {
        if(player != activePlayer) {
            player.sendInputData(msgId, s);
        }
    }

    public void sendEndBattle() {
        for (Player player : players) {
            player.sendEndBattle(activePlayer == null ? 0 : activePlayer.id);
        }
    }
}

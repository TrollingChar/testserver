import java.util.*;

/**
 * Created by UserName on 12.07.2016.
 */
public class Room {
    LinkedList<Player> players;
    Set<Player> readyPlayers;

    Player activePlayer;
    LinkedList<Player> playerQueue;

    public Room(Collection<Player> players) {
        // это 2 разных списка, у них разные указатели
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
        readyPlayers.add(player);
        if (!alive) {
            playerQueue.remove(player);
        }
        if(players.size() == readyPlayers.size()) {
            newTurn();
        }
    }

    public void receiveInputData(Player player, String s) {
        if(player == activePlayer) {
            sendInputData(s);
        }
    }

    public void sendStartBattle() {
        for (Player player : players) {
            player.sendStartBattle(players);
        }
    }

    public void sendHisTurn(int id) {
        for (Player player : players) {
            player.sendHisTurn(id);
        }
    }

    public void sendInputData(String s) {
        for (Player player : players) {
            if(player != activePlayer) {
                player.sendInputData(s);
            }
        }
    }

    public void sendEndBattle() {
        for (Player player : players) {
            player.sendEndBattle(activePlayer == null ? 0 : activePlayer.id);
        }
    }
}

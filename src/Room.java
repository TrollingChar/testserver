import java.util.Queue;

/**
 * Created by UserName on 12.07.2016.
 */
public class Room {
    int randomSeed;
    int playersNotReady;        // от скольки игроков мы ожидаем сингал готовности
    int playersAlive;           // число оставшихся в игре игроков, нужно для условий победы/ничьей

    Player activePlayer = null;
    Queue<Player> players;

    void init() {
        // отправить всем клиентам сообщение о начале боя

        playersNotReady =
        playersAlive = players.size();

        for(Player player : players) {
            player.canGetTurn = true;
        }
    }

    void setReady(Player player, Boolean alive) {
        playersNotReady--;
        if(!alive) {
            player.canGetTurn = false;
        }
    }

    void newTurn() {
        if(playersAlive == 0) {
            endGame(null);
        }
        if (playersAlive == 1) {

        }
    }

    void endGame(Player winner) {
        if(winner == null) {

        } else {

        }
    }
}

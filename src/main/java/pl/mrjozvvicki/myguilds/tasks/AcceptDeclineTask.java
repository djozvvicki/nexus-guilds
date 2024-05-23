package pl.mrjozvvicki.myguilds.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.mrjozvvicki.myguilds.utils.Chat;

public class AcceptDeclineTask extends BukkitRunnable {
    private final Player player;
    private int timeLeft = 30; // czas na akceptację lub odrzucenie

    public AcceptDeclineTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (timeLeft <= 0) {
            Chat.sendMessage(player, "&7Czas na akceptację minął!");
            this.cancel();
            return;
        }

        timeLeft--;
    }
}
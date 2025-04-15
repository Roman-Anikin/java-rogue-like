package domain;

import entities.characters.Monster;
import entities.characters.Player;
import entities.items.Item;

import java.util.List;

public class GameMessenger {

    private final StringBuilder message;

    public GameMessenger() {
        message = new StringBuilder();
    }

    public String getMessage() {
        return message.toString();
    }

    public void addItemMessage(Item item) {
        message.append("You have found new ").append(item).append("!");
    }

    public void addHitMessage(int damage, Monster monster) {
        String name = monster.getType().toString().toLowerCase();
        if (damage == 0) {
            message.append("Your attack against ").append(name).append(" missed.");
            return;
        }
        String health;
        if (monster.getHealth() > 0) {
            health = "He has left " + monster.getHealth() + " health. ";
        } else {
            health = "He died. ";
        }
        message.append("You hit ").append(name).append(" for ").append(damage).append(" damage. ")
                .append(health);
    }

    public void addLootMessage(int value) {
        message.append("You received ").append(value).append(" gold.");
    }

    public String getStats(Player player, int level) {
        String duration = player.getBuff() == null ? "0" : String.valueOf(player.getBuff().getDuration());
        return "LEVEL:" + level +
                "    HEALTH:" + player.getHealth() + "(" + player.getMaxHealth() + ")" +
                "    STR:" + player.getStr() +
                "    DEX:" + player.getDex() +
                "    GOLD:" + player.getBackpack().getGold() +
                "    BUFF:" + duration +
                "    SLEEP:" + player.getSleep().getDuration();
    }

    public String getBackpack(List<Item> items) {
        if (items == null) {
            return "You can't use items while sleep!";
        }
        if (items.isEmpty()) {
            return "";
        }
        StringBuilder text = new StringBuilder();
        text.append("Which ").append(items.get(0).getType()).append(" you choose? (1-9)  ");
        for (int i = 1; i <= items.size(); i++) {
            text.append(i).append(") ").append(items.get(i - 1).toPrint()).append("  ");
        }
        return text.toString();
    }

    public void addWinMessage(String name) {
        message.append("Congratulations ").append(name).append("! You win!");
    }

    public void addDefeatMessage(String name) {
        message.append(name).append(" has been defeated!");
    }

    public void clear() {
        message.setLength(0);
    }
}

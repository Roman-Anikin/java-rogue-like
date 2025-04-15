package domain;

public class GameStatistics {

    private String playerName;
    private int gold;
    private int level;
    private int defeatedMonsters;
    private int usedFoods;
    private int usedElixirs;
    private int usedScrolls;
    private int hits;
    private int misses;
    private int steps;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getGold() {
        return gold;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getLevel() {
        return level;
    }

    public int getDefeatedMonsters() {
        return defeatedMonsters;
    }

    public int getUsedFoods() {
        return usedFoods;
    }

    public int getUsedElixirs() {
        return usedElixirs;
    }

    public int getUsedScrolls() {
        return usedScrolls;
    }

    public int getHits() {
        return hits;
    }

    public int getMisses() {
        return misses;
    }

    public int getSteps() {
        return steps;
    }

    public void increaseGold(int value) {
        gold += value;
    }

    public void increaseDifficulty(int level) {
        this.level = level;
    }

    public void increaseDefeatedMonsters() {
        defeatedMonsters++;
    }

    public void increaseUsedFoods() {
        usedFoods++;
    }

    public void increaseUsedElixirs() {
        usedElixirs++;
    }

    public void increaseUsedScrolls() {
        usedScrolls++;
    }

    public void increaseHits() {
        hits++;
    }

    public void increaseMisses() {
        misses++;
    }

    public void increaseSteps() {
        steps++;
    }

    @Override
    public String toString() {
        return String.format("Player name: %-14s" +
                        "Gold: %-18d" +
                        "Level: %-9d" +
                        "Slayed monsters: %-10d" +
                        "Food eaten: %-10d" +
                        "," +
                        "Elixir drunk: %-13d" +
                        "Scrolls read: %-10d" +
                        "Hits: %-10d" +
                        "Misses: %-19d" +
                        "Cells passed: %-10d",
                playerName, gold, level, defeatedMonsters, usedFoods, usedElixirs, usedScrolls, hits, misses, steps);
    }
}
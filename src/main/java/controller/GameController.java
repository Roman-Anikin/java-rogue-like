package controller;

import domain.GameService;
import domain.GameStatistics;

import java.util.List;

public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    public void startNewGame() {
        service.startNewGame();
    }

    public boolean loadGame() {
        return service.loadGame();
    }

    public void setPlayerName(String name) {
        service.setPlayerName(name);
    }

    public void playerAction(char direction) {
        service.playerAction(direction);
    }

    public char[][] getMap() {
        return service.getGameMap();
    }

    public void useItem(char type, char number) {
        service.useItem(type, number);
    }

    public String getStats() {
        return service.getStatsMessage();
    }

    public String getBackpack(char type) {
        return service.getBackpackMessage(type);
    }

    public String getMessage() {
        return service.getMessage();
    }

    public void clearMessage() {
        service.clearMessage();
    }

    public boolean isPlayerWin() {
        return service.isPlayerWin();
    }

    public boolean isPlayerDead() {
        return service.isPlayerDead();
    }

    public List<GameStatistics> getStatistic() {
        return service.getStatistics();
    }
}

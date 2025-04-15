package presentation;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import controller.GameController;
import domain.GameStatistics;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration.BoldMode.EVERYTHING;
import static domain.GameGenerator.HEIGHT;
import static domain.GameGenerator.WIDTH;
import static util.GameUtils.*;

public class GameRender {

    private final GameController controller;
    private Screen screen;
    private TextGraphics tg;

    public GameRender(GameController controller) {
        this.controller = controller;
        try {
            Font font = new Font("Lucida Console", Font.PLAIN, 14);
            SwingTerminalFontConfiguration config = new SwingTerminalFontConfiguration(true, EVERYTHING,
                    font);
            DefaultTerminalFactory factory = new DefaultTerminalFactory()
                    .setTerminalEmulatorFontConfiguration(config)
                    .setForceTextTerminal(false)
                    .setInitialTerminalSize(new TerminalSize(WIDTH, HEIGHT))
                    .setTerminalEmulatorTitle("Rogue");
            screen = factory.createScreen();
            screen.setCursorPosition(null);
            screen.startScreen();
            tg = screen.newTextGraphics();
            tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void init() {
        try {
            drawSelectGameMode();
            readGameSelection();
            updateScreen();
            readInput();
        } catch (Exception ignored) {
            shutdown();
        }
    }

    private void readGameSelection() throws IOException {
        int number;
        while (true) {
            KeyStroke key = screen.readInput();
            number = Character.getNumericValue(key.getCharacter());
            if (number == 1) {
                controller.startNewGame();
                controller.setPlayerName(readPlayerName());
                break;
            }
            if (number == 2) {
                if (controller.loadGame()) {
                    break;
                }
                String text = "Save file corrupt or empty. Try again or start new game.";
                tg.putString(WIDTH / 2 - text.length() / 2, 14, text);
                screen.refresh();
            }
        }
    }

    private String readPlayerName() throws IOException {
        String message = "Enter your character name:  ";
        int x = WIDTH / 3, y = 10, length = message.length();
        screen.clear();
        tg.putString(x, y, message);
        screen.refresh();
        StringBuilder name = new StringBuilder();
        while (true) {
            char ch = screen.readInput().getCharacter();
            if (ch == '\n') {
                break;
            } else if (ch == ' ' && !name.isEmpty()) {
                name.setLength(name.length() - 1);
                tg.putString(length + x + name.length(), y, " ");
                screen.refresh();
            } else {
                name.append(ch);
            }
            tg.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
            tg.putString(length + x, y, name.toString());
            screen.refresh();
        }
        return name.toString();
    }

    private void updateScreen() throws IOException {
        screen.clear();
        drawGameMap();
        drawPlayerStats();
        drawGameLog();
        screen.refresh();
    }

    private void readInput() throws IOException {
        while (true) {
            char key = screen.readInput().getCharacter();
            if (key == SHOW_STATISTICS) {
                drawStatistics();
                screen.readInput();
            } else if (isBackpackKey(key)) {
                drawBackpack(key);
                controller.useItem(key, screen.readInput().getCharacter());
            } else if (isMovementKey(key)) {
                controller.playerAction(key);
            }
            updateScreen();
            if (controller.isPlayerWin() || controller.isPlayerDead()) {
                drawStatistics();
                drawEndOfGame();
                screen.readInput();
                break;
            }
        }
        init();
    }

    private void drawSelectGameMode() throws IOException {
        int x = WIDTH / 2 - 12, y = 10;
        screen.clear();
        tg.putString(x, y - 1, "Choose game mode: (1,2)");
        tg.putString(x + 3, ++y, "1) Start new game");
        tg.putString(x + 3, ++y, "2) Load save file");
        screen.refresh();
    }

    private void drawGameMap() {
        char[][] map = controller.getMap();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                drawDungeon(map[i][j], i, j);
                drawItems(map[i][j], i, j);
                drawCharacters(map[i][j], i, j);
            }
        }
    }

    private void drawDungeon(char ch, int x, int y) {
        switch (ch) {
            case WALL:
                tg.setForegroundColor(TextColor.ANSI.YELLOW);
                tg.setBackgroundColor(TextColor.ANSI.YELLOW);
                tg.setCharacter(x, y, WALL);
                break;
            case FLOOR:
                tg.setForegroundColor(TextColor.ANSI.GREEN);
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
                tg.setCharacter(x, y, FLOOR);
                break;
            case DUNGEON_EXIT:
                tg.setForegroundColor(TextColor.ANSI.BLUE_BRIGHT);
                tg.setBackgroundColor(TextColor.ANSI.BLACK);
                tg.setCharacter(x, y, DUNGEON_EXIT);
                break;
            case HALLWAY:
                tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                tg.setBackgroundColor(TextColor.ANSI.WHITE);
                tg.setCharacter(x, y, FLOOR);
        }
    }

    private void drawItems(char ch, int x, int y) {
        tg.setForegroundColor(TextColor.ANSI.CYAN_BRIGHT);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        switch (ch) {
            case GOLD:
                tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                tg.setCharacter(x, y, GOLD);
                break;
            case FOOD:
                tg.setCharacter(x, y, FOOD);
                break;
            case ELIXIR:
                tg.setCharacter(x, y, ELIXIR);
                break;
            case SCROLL:
                tg.setCharacter(x, y, SCROLL);
                break;
            case WEAPON:
                tg.setCharacter(x, y, WEAPON);
        }
    }

    private void drawCharacters(char ch, int x, int y) {
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        switch (ch) {
            case PLAYER:
                tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                tg.setCharacter(x, y, PLAYER);
                break;
            case ZOMBIE:
                tg.setForegroundColor(TextColor.ANSI.GREEN);
                tg.setCharacter(x, y, ZOMBIE);
                break;
            case VAMPIRE:
                tg.setForegroundColor(TextColor.ANSI.RED);
                tg.setCharacter(x, y, VAMPIRE);
                break;
            case GHOUL:
                tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                tg.setCharacter(x, y, GHOUL);
                break;
            case OGRE:
                tg.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                tg.setCharacter(x, y, OGRE);
                break;
            case SNAKE:
                tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
                tg.setCharacter(x, y, SNAKE);
        }
    }

    private void drawPlayerStats() throws IOException {
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.putString(WIDTH / 2 - controller.getStats().length() / 2, HEIGHT - 2, controller.getStats());
        screen.refresh();
    }

    private void drawBackpack(char type) throws IOException {
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.putString(5, HEIGHT - 4, controller.getBackpack(type));
        screen.refresh();
    }

    private void drawGameLog() throws IOException {
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.putString(5, HEIGHT - 5, controller.getMessage());
        controller.clearMessage();
        screen.refresh();
    }

    private void drawEndOfGame() throws IOException {
        tg.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
        tg.setBackgroundColor(TextColor.ANSI.BLACK);
        tg.putString(WIDTH / 2 - 10, 3, controller.getMessage());
        screen.refresh();
    }

    private void drawStatistics() throws IOException {
        screen.clear();
        List<GameStatistics> statistics = controller.getStatistic();
        int y = 6;
        for (GameStatistics statistic : statistics) {
            String[] stat = statistic.toString().split(",");
            tg.putString(5, y++, stat[0]);
            tg.putString(5, y++, stat[1]);
            tg.drawLine(5, y, stat[1].length(), y, '─');
            y++;
        }
        tg.putString(5, ++y, "Press any key to continue...");
        screen.refresh();
    }

    private boolean isBackpackKey(char key) {
        return key == USE_WEAPON || key == USE_FOOD || key == USE_ELIXIR || key == USE_SCROLL;
    }

    private boolean isMovementKey(char key) {
        return key == UP || key == DOWN || key == LEFT || key == RIGHT;
    }

    private void shutdown() {
        try {
            screen.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

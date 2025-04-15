package domain;

import entities.characters.*;
import entities.items.Backpack;
import entities.items.Gold;
import entities.items.Item;
import entities.items.ItemType;
import entities.spaces.Exit;
import entities.spaces.Hallway;
import entities.spaces.Room;
import repository.GameRepository;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static util.GameUtils.*;

public class GameService {

    private static final int BUFF_DURATION = 10;
    private static final int BASE_HIT_CHANCE = 60;
    private static final int CONDITION_DURATION = 1;

    private GameGenerator generator;
    private final GameMessenger messenger;
    private List<GameStatistics> statistics;
    private GameStatistics curStatistic;
    private final GameRepository repository;
    private final char[][] gameMap;

    public GameService() {
        generator = new GameGenerator();
        messenger = new GameMessenger();
        repository = new GameRepository();
        gameMap = new char[GameGenerator.WIDTH][GameGenerator.HEIGHT];
    }

    public String getMessage() {
        return messenger.getMessage();
    }

    public List<GameStatistics> getStatistics() {
        statistics.sort(Comparator.comparingInt(GameStatistics::getGold).reversed());
        return statistics;
    }

    public char[][] getGameMap() {
        return gameMap;
    }

    public void startNewGame() {
        generator.resetDifficulty();
        generator.generatePlayer();
        messenger.clear();
        loadStatistics();
        statistics.add(new GameStatistics());
        curStatistic = statistics.get(statistics.size() - 1);
        initGame();
    }

    public boolean loadGame() {
        GameGenerator loaded = repository.loadGame();
        if (loaded == null) {
            return false;
        }
        generator = loaded;
        loadStatistics();
        curStatistic = statistics.get(statistics.size() - 1);
        fillGameMap();
        return true;
    }

    private void loadStatistics() {
        statistics = repository.loadStatistics();
        if (statistics == null) {
            statistics = new ArrayList<>();
        }
    }

    private void initGame() {
        generator.generateGameLevel();
        curStatistic.increaseDifficulty(generator.getDifficultyLevel());
        clearGameMap();
        fillGameMap();
    }

    public void setPlayerName(String name) {
        generator.getPlayer().setName(name);
        curStatistic.setPlayerName(name);
    }

    public boolean isPlayerWin() {
        if (generator.getDifficultyLevel() != GameGenerator.MAX_LEVEL) {
            return false;
        }
        int x = generator.getPlayer().getX(), y = generator.getPlayer().getY();
        for (Room room : generator.getRooms()) {
            if (room.getTransition() != null && room.getTransition().getX() == x && room.getTransition().getY() == y) {
                messenger.addWinMessage(generator.getPlayer().getName());
                repository.clearGameSave();
                repository.saveStatistics(statistics);
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerDead() {
        Player player = generator.getPlayer();
        if (player.getHealth() == 0) {
            messenger.addDefeatMessage(player.getName());
            repository.clearGameSave();
            repository.saveStatistics(statistics);
            return true;
        }
        return false;
    }

    public String getStatsMessage() {
        return messenger.getStats(generator.getPlayer(), generator.getDifficultyLevel());
    }

    public String getBackpackMessage(char type) {
        if (generator.getPlayer().isSleeping()) {
            return messenger.getBackpack(null);
        }
        return messenger.getBackpack(getItemsByType(type));
    }

    public void clearMessage() {
        messenger.clear();
    }

    private void clearGameMap() {
        Arrays.stream(gameMap).forEach(arr -> Arrays.fill(arr, SPACE));
    }

    private void fillGameMap() {
        addRoomsToMap();
        addHallwaysToMap();
        addItemsToMap();
        addMonstersToMap();
        gameMap[generator.getPlayer().getX()][generator.getPlayer().getY()] = PLAYER;
    }

    private void addRoomsToMap() {
        for (Room room : generator.getRooms()) {
            for (int i = room.getX() - 1; i <= room.getX() + room.getWidth(); i++) {
                for (int j = room.getY() - 1; j <= room.getY() + room.getHeight(); j++) {
                    if (room.isVisited() && (i == room.getX() - 1
                            || i == room.getX() + room.getWidth()
                            || j == room.getY() - 1
                            || j == room.getY() + room.getHeight())) {
                        gameMap[i][j] = WALL;
                    } else if (room.isCurrent()) {
                        gameMap[i][j] = FLOOR;
                    } else {
                        gameMap[i][j] = SPACE;
                    }
                }
            }
            for (Exit exit : room.getExits()) {
                gameMap[exit.getX()][exit.getY()] = ROOM_EXIT;
            }
            if (room.getTransition() != null && room.isCurrent()) {
                gameMap[room.getTransition().x][room.getTransition().y] = DUNGEON_EXIT;
            }
        }
    }

    private void addHallwaysToMap() {
        for (Hallway hallway : generator.getHallways()) {
            if (!hallway.isVisited()) {
                continue;
            }
            for (Point point : hallway.getCoordinates()) {
                gameMap[point.x][point.y] = HALLWAY;
            }
        }
    }

    private void addItemsToMap() {
        for (Item item : generator.getItems()) {
            Room curRoom = getCurrentRoom();
            if ((curRoom != null && curRoom.contains(item.getX(), item.getY()))
                    || isInHallway(item.getX(), item.getY())) {
                switch (item.getType()) {
                    case GOLD:
                        gameMap[item.getX()][item.getY()] = GOLD;
                        break;
                    case FOOD:
                        gameMap[item.getX()][item.getY()] = FOOD;
                        break;
                    case ELIXIR:
                        gameMap[item.getX()][item.getY()] = ELIXIR;
                        break;
                    case SCROLL:
                        gameMap[item.getX()][item.getY()] = SCROLL;
                        break;
                    case WEAPON:
                        gameMap[item.getX()][item.getY()] = WEAPON;
                        break;
                }
            }
        }
    }

    private void addMonstersToMap() {
        for (Monster monster : generator.getMonsters()) {
            Room curRoom = getCurrentRoom();
            if ((curRoom != null && curRoom.contains(monster.getX(), monster.getY()))
                    || monster.isChasing()) {
                switch (monster.getType()) {
                    case ZOMBIE:
                        gameMap[monster.getX()][monster.getY()] = ZOMBIE;
                        break;
                    case VAMPIRE:
                        gameMap[monster.getX()][monster.getY()] = VAMPIRE;
                        break;
                    case GHOUL:
                        Ghoul ghoul = (Ghoul) monster;
                        gameMap[monster.getX()][monster.getY()] = ghoul.isInvisible() ? FLOOR : GHOUL;
                        break;
                    case OGRE:
                        gameMap[monster.getX()][monster.getY()] = OGRE;
                        break;
                    case SNAKE:
                        gameMap[monster.getX()][monster.getY()] = SNAKE;
                        break;
                }
            }
        }
    }

    private Room getCurrentRoom() {
        return generator.getRooms()
                .stream()
                .filter(Room::isCurrent)
                .findAny()
                .orElse(null);
    }

    public void playerAction(char direction) {
        Player player = generator.getPlayer();
        if (player.isSleeping()) {
            player.getSleep().decreaseDuration();
        } else {
            Monster target = getPlayerTarget(player, direction);
            if (target != null) {
                playerAttack(player, target);
            } else {
                movePlayer(player, direction);
                checkItemPosition(player);
                checkLevelExitPosition(getCurrentRoom(), player);
            }
        }
        buffTick(player.getBuff());
        monstersAction();
    }

    private Monster getPlayerTarget(Player player, char direction) {
        int x = 0, y = 0;
        y = switch (direction) {
            case UP -> {
                x = player.getX();
                yield player.getY() - 1;
            }
            case DOWN -> {
                x = player.getX();
                yield player.getY() + 1;
            }
            case LEFT -> {
                x = player.getX() - 1;
                yield player.getY();
            }
            case RIGHT -> {
                x = player.getX() + 1;
                yield player.getY();
            }
            default -> y;
        };
        for (Monster monster : generator.getMonsters()) {
            if (monster.getX() == x && monster.getY() == y) {
                return monster;
            }
        }
        return null;
    }

    private void playerAttack(Player player, Monster monster) {
        if (!checkHit(player, monster)) {
            messenger.addHitMessage(0, monster);
            curStatistic.increaseMisses();
            return;
        }
        if (monster.getType() == MonsterType.VAMPIRE) {
            Vampire vampire = (Vampire) monster;
            if (vampire.isMissable()) {
                vampire.loseEvasion();
                messenger.addHitMessage(0, monster);
                curStatistic.increaseMisses();
                return;
            }
        }
        takeDamage(monster, player.getStr());
        messenger.addHitMessage(player.getStr(), monster);
        curStatistic.increaseHits();
        if (monster.getHealth() == 0) {
            collectLoot(monster);
            generator.getMonsters().remove(monster);
            curStatistic.increaseDefeatedMonsters();
        }
    }

    private void collectLoot(Monster monster) {
        int retrievedGold = (monster.getHostility() + monster.getStr()
                + monster.getDex() + monster.getMaxHealth() / 3);
        messenger.addLootMessage(retrievedGold);
        addItemToBackpack(new Gold(retrievedGold), generator.getPlayer().getBackpack());
    }

    private void movePlayer(Player player, char direction) {
        for (int i = 0; i < player.getSpeed(); i++) {
            int x = player.getX(), y = player.getY();
            if (direction == UP && (isInRoom(x, y - 1) || isInHallway(x, y - 1))) {
                player.setY(y - 1);
            } else if (direction == DOWN && (isInRoom(x, y + 1) || isInHallway(x, y + 1))) {
                player.setY(y + 1);
            } else if (direction == LEFT && (isInRoom(x - 1, y) || isInHallway(x - 1, y))) {
                player.setX(x - 1);
            } else if (direction == RIGHT && (isInRoom(x + 1, y) || isInHallway(x + 1, y))) {
                player.setX(x + 1);
            }
            if (x != player.getX() || y != player.getY()) {
                curStatistic.increaseSteps();
            }
            changeMapAppearances(player, x, y);
            checkChase();
        }
    }

    private void changeMapAppearances(Player player, int oldX, int oldY) {
        if (isInRoomOrExit(player.getX(), player.getY())) {
            setRoomIsVisited(player.getX(), player.getY());
            setRoomIsCurrent(player.getX(), player.getY(), true);
        } else {
            setRoomIsCurrent(oldX, oldY, false);
        }
        if (isInHallway(player.getX(), player.getY())) {
            setHallwayIsVisited(player.getX(), player.getY());
        }
    }

    private boolean isInRoom(int x, int y) {
        return generator.getRooms()
                .stream()
                .anyMatch(room -> room.contains(x, y));
    }

    private boolean isInHallway(int x, int y) {
        return generator.getHallways()
                .stream()
                .anyMatch(hallway -> hallway.contains(x, y));
    }

    private boolean isInRoomOrExit(int x, int y) {
        return generator.getRooms()
                .stream()
                .anyMatch(room -> (x >= room.getX() - 1 && x <= room.getX() + room.getWidth()
                        && y >= room.getY() - 1 && y <= room.getY() + room.getHeight()));
    }

    private void setRoomIsCurrent(int x, int y, boolean isCurrent) {
        generator.getRooms()
                .stream()
                .filter(room -> room.getExits()
                        .stream()
                        .anyMatch(exit -> exit.getX() == x && exit.getY() == y))
                .findAny()
                .ifPresent(room -> room.setCurrent(isCurrent));
    }

    private void setRoomIsVisited(int x, int y) {
        generator.getRooms()
                .stream()
                .filter(room -> room.getExits()
                        .stream()
                        .anyMatch(exit -> exit.getX() == x && exit.getY() == y))
                .findAny()
                .ifPresent(room -> room.setVisited(true));
    }

    private void setHallwayIsVisited(int x, int y) {
        generator.getHallways()
                .stream()
                .filter(hallway ->
                        (hallway.getStart().getX() == x && hallway.getStart().getY() == y)
                                || hallway.getEnd().getX() == x && hallway.getEnd().getY() == y)
                .findAny()
                .ifPresent(hallway -> hallway.setVisited(true));
    }

    private void checkItemPosition(Player player) {
        List<Item> items = generator.getItems();
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (player.getX() == items.get(i).getX() && player.getY() == items.get(i).getY()) {
                messenger.addItemMessage(items.get(i));
                index = i;
                break;
            }
        }
        if (index == -1) {
            return;
        }
        if (addItemToBackpack(items.get(index), player.getBackpack())) {
            generator.getItems().remove(index);
        }
    }

    private boolean addItemToBackpack(Item item, Backpack backpack) {
        if (item.getType() == ItemType.GOLD) {
            for (Item i : backpack.getItems()) {
                if (i.getType() == ItemType.GOLD) {
                    ((Gold) i).addGold(item.getValue());
                    curStatistic.increaseGold(item.getValue());
                    return true;
                }
            }
            backpack.getItems().add(item);
            curStatistic.increaseGold(item.getValue());
            return true;
        } else {
            int count = 0;
            for (Item i : backpack.getItems()) {
                if (i.getType() == item.getType()) {
                    count++;
                }
            }
            if (count < 9) {
                backpack.getItems().add(item);
                return true;
            }
        }
        return false;
    }

    private void checkLevelExitPosition(Room room, Player player) {
        if (room == null || room.getTransition() == null) {
            return;
        }
        if (player.getX() == room.getTransition().getX() && player.getY() == room.getTransition().getY()
                && generator.getDifficultyLevel() < GameGenerator.MAX_LEVEL) {
            initGame();
            repository.saveGame(generator);
            repository.saveStatistics(statistics);
        }
    }

    public void useItem(char type, char number) {
        Player player = generator.getPlayer();
        List<Item> items = getItemsByType(type);
        int index = Character.getNumericValue(number);
        if (player.isSleeping()) {
            player.getSleep().decreaseDuration();
            monstersAction();
            buffTick(player.getBuff());
        } else if (index > 0 && index <= items.size()) {
            Item item = items.get(index - 1);
            consumeItem(player, item);
            player.getBackpack().getItems().remove(item);
            if (item.getType() != ItemType.ELIXIR) {
                buffTick(player.getBuff());
            }
            monstersAction();
        }
    }

    private List<Item> getItemsByType(char type) {
        return generator.getPlayer().getBackpack().getItems()
                .stream()
                .filter(item -> item.getType() == ItemType.getType(type))
                .collect(Collectors.toList());
    }

    private void consumeItem(Player player, Item item) {
        switch (item.getType()) {
            case WEAPON:
                equipWeapon(player, item);
                break;
            case FOOD:
                consumeFood(player, item);
                break;
            case ELIXIR:
                consumeElixir(player, item);
                break;
            case SCROLL:
                consumeScroll(player, item);
        }
    }

    private void equipWeapon(Player player, Item weapon) {
        if (player.getWeapon() != null) {
            dropWeapon(player, player.getWeapon());
        }
        player.setWeapon(weapon);
        player.setStr(player.getStr() + weapon.getValue());
    }

    private void dropWeapon(Player player, Item weapon) {
        int x = player.getX(), y = player.getY();
        if (isInRoom(x, y - 1) || isInHallway(x, y - 1)) {
            weapon.setCoordinates(x, y - 1);
        } else if (isInRoom(x, y + 1) || isInHallway(x, y + 1)) {
            weapon.setCoordinates(x, y + 1);
        } else if (isInRoom(x - 1, y) || isInHallway(x - 1, y)) {
            weapon.setCoordinates(x - 1, y);
        } else if (isInRoom(x + 1, y) || isInHallway(x + 1, y)) {
            weapon.setCoordinates(x + 1, y);
        }
        player.setStr(player.getStr() - weapon.getValue());
        generator.getItems().add(weapon);
    }

    private void consumeFood(Player player, Item food) {
        player.setHealth(player.getHealth() + food.getValue());
        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
        curStatistic.increaseUsedFoods();
    }

    private void consumeElixir(Player player, Item elixir) {
        if (player.getBuff() != null) {
            endBuff(player, player.getBuff());
        }
        player.setBuff(new Buff(elixir.getSubType(), elixir.getValue(), BUFF_DURATION));
        switch (elixir.getSubType()) {
            case STR:
                player.setStr(player.getStr() + elixir.getValue());
                break;
            case DEX:
                player.setDex(player.getDex() + elixir.getValue());
                break;
            case HP:
                player.setHealth(player.getHealth() + elixir.getValue());
                player.setMaxHealth(player.getMaxHealth() + elixir.getValue());
        }
        curStatistic.increaseUsedElixirs();
    }

    private void consumeScroll(Player player, Item scroll) {
        switch (scroll.getSubType()) {
            case STR:
                player.setStr(player.getStr() + scroll.getValue());
                break;
            case DEX:
                player.setDex(player.getDex() + scroll.getValue());
                break;
            case HP:
                player.setMaxHealth(player.getMaxHealth() + scroll.getValue());
        }
        curStatistic.increaseUsedScrolls();
    }

    private void buffTick(Buff buff) {
        if (buff == null) {
            return;
        }
        buff.decreaseDuration();
        if (buff.getDuration() == 0) {
            endBuff(generator.getPlayer(), buff);
        }
    }

    private void endBuff(Player player, Buff buff) {
        switch (buff.getType()) {
            case STR:
                player.setStr(player.getStr() - buff.getValue());
                break;
            case DEX:
                player.setDex(player.getDex() - buff.getValue());
                break;
            case HP:
                player.setHealth(player.getHealth() - buff.getValue());
                if (player.getHealth() < 1) {
                    player.setHealth(1);
                }
                player.setMaxHealth(player.getMaxHealth() - buff.getValue());
                if (player.getHealth() > player.getMaxHealth()) {
                    player.setHealth(player.getMaxHealth());
                }
        }
        player.setBuff(null);
    }

    private void monstersAction() {
        for (Monster monster : generator.getMonsters()) {
            int x = monster.getX(), y = monster.getY();
            if (containsPlayer(x, y - 1) || containsPlayer(x, y + 1)
                    || containsPlayer(x - 1, y) || containsPlayer(x + 1, y)) {
                monsterAttack(monster);
                continue;
            }
            for (int i = 0; i < monster.getSpeed(); i++) {
                char direction = generator.generateMonsterDirection();
                if (monster.isChasing()) {
                    if (monster.getType() == MonsterType.GHOUL) {
                        Ghoul ghoul = (Ghoul) monster;
                        ghoul.setInvisible(false);
                    }
                    chasePlayer(monster);
                } else if (monster.getType() == MonsterType.GHOUL) {
                    moveGhoul((Ghoul) monster);
                } else if (monster.getType() == MonsterType.SNAKE) {
                    moveSnake((Snake) monster, direction);
                } else {
                    moveMonster(monster, direction);
                }
                checkChase();
            }
        }
        fillGameMap();
    }

    private void moveGhoul(Ghoul ghoul) {
        Random rnd = generator.getRnd();
        ghoul.setInvisible(rnd.nextBoolean());
        Room curRoom = generator.getRooms()
                .stream()
                .filter(room -> room.contains(ghoul.getX(), ghoul.getY()))
                .findFirst()
                .orElseThrow(RuntimeException::new);
        int x, y;
        do {
            x = curRoom.getX() + rnd.nextInt(curRoom.getWidth());
            y = curRoom.getY() + rnd.nextInt(curRoom.getHeight());
        }
        while (!isInRoom(x, y) || containsPlayer(x, y) || containsMonster(x, y));
        ghoul.move(x, y);
    }

    private void moveSnake(Snake snake, char direction) {
        int x = snake.getX(), y = snake.getY();
        int side = generator.getRnd().nextBoolean() ? 1 : -1;
        if (direction == UP && isInRoom(x + side, y - 1) && !containsMonster(x + side, y - 1)) {
            snake.move(x + side, y - 1);
        } else if (direction == DOWN && isInRoom(x + side, y + 1) && !containsMonster(x + side, y + 1)) {
            snake.move(x + side, y + 1);
        } else if (direction == LEFT && isInRoom(x - 1, y + side) && !containsMonster(x - 1, y + side)) {
            snake.move(x - 1, y + side);
        } else if (direction == RIGHT && isInRoom(x + 1, y + side) && !containsMonster(x + 1, y + side)) {
            snake.move(x + 1, y + side);
        }
    }

    private void moveMonster(Monster monster, char direction) {
        int x = monster.getX(), y = monster.getY();
        if (direction == UP && isInRoom(x, y - 1) && !containsMonster(x, y - 1)) {
            monster.setY(y - 1);
        } else if (direction == DOWN && isInRoom(x, y + 1) && !containsMonster(x, y + 1)) {
            monster.setY(y + 1);
        } else if (direction == LEFT && isInRoom(x - 1, y) && !containsMonster(x - 1, y)) {
            monster.setX(x - 1);
        } else if (direction == RIGHT && isInRoom(x + 1, y) && !containsMonster(x + 1, y)) {
            monster.setX(x + 1);
        }
    }

    private boolean containsMonster(int x, int y) {
        return generator.getMonsters()
                .stream()
                .anyMatch(monster -> monster.getX() == x && monster.getY() == y);
    }

    private void checkChase() {
        Player player = generator.getPlayer();
        generator.getMonsters()
                .stream()
                .filter(monster -> player.getX() >= monster.getX() - monster.getHostility()
                        && player.getX() <= monster.getX() + monster.getHostility()
                        && (player.getY() >= monster.getY() - monster.getHostility()
                        && player.getY() <= monster.getY() + monster.getHostility())
                        && isInRoomOrExit(player.getX(), player.getY()))
                .forEach(Monster::setChasing);
    }

    private void chasePlayer(Monster monster) {
        Player player = generator.getPlayer();
        int x = monster.getX(), y = monster.getY();
        if (y > player.getY() && (isInRoom(x, y - 1) || isInHallway(x, y - 1))
                && !containsMonster(x, y - 1) && !containsPlayer(x, y - 1)) {
            monster.setY(y - 1);
        } else if (y < player.getY() && (isInRoom(x, y + 1) || isInHallway(x, y + 1))
                && !containsMonster(x, y + 1) && !containsPlayer(x, y + 1)) {
            monster.setY(y + 1);
        } else if (x > player.getX() && (isInRoom(x - 1, y) || isInHallway(x - 1, y))
                && !containsMonster(x - 1, y) && !containsPlayer(x - 1, y)) {
            monster.setX(x - 1);
        } else if (x < player.getX() && (isInRoom(x + 1, y) || isInHallway(x + 1, y))
                && !containsMonster(x + 1, y) && !containsPlayer(x + 1, y)) {
            monster.setX(x + 1);
        }
    }

    private boolean containsPlayer(int x, int y) {
        return generator.getPlayer().getX() == x && generator.getPlayer().getY() == y;
    }

    private void monsterAttack(Monster monster) {
        Player player = generator.getPlayer();
        if (monster.getType() == MonsterType.GHOUL) {
            Ghoul ghoul = (Ghoul) monster;
            ghoul.setInvisible(false);
        }
        if (monster.getType() == MonsterType.OGRE) {
            ogreAttack(player, (Ogre) monster);
            return;
        }
        if (!checkHit(monster, player)) {
            return;
        }
        takeDamage(player, monster.getStr());
        switch (monster.getType()) {
            case VAMPIRE:
                Vampire vampire = (Vampire) monster;
                player.setMaxHealth(player.getMaxHealth() - vampire.lifeSteal());
                break;
            case SNAKE:
                Snake snake = (Snake) monster;
                if (snake.tryPutToSleep()) {
                    player.setSleep(new Condition(CONDITION_DURATION));
                }
        }
    }

    private void ogreAttack(Player player, Ogre ogre) {
        if (ogre.isResting()) {
            ogre.getRest().decreaseDuration();
            ogre.setCounterAttacking(true);
            return;
        }
        if (ogre.isCounterAttacking() && checkHit(ogre, player)) {
            takeDamage(player, ogre.counterAttack());
            ogre.setCounterAttacking(false);
        }
        if (checkHit(ogre, player)) {
            takeDamage(player, ogre.getStr());
        }
        ogre.setRest(new Condition(CONDITION_DURATION));
    }

    private boolean checkHit(GameCharacter attacker, GameCharacter target) {
        int hitChance = BASE_HIT_CHANCE + attacker.getDex() + attacker.getSpeed() - target.getDex() - target.getSpeed();
        return generator.getRnd().nextInt(100) + 1 <= hitChance;
    }

    private void takeDamage(GameCharacter target, int damage) {
        target.setHealth(target.getHealth() - damage);
        if (target.getHealth() < 0) {
            target.setHealth(0);
        }
    }
}
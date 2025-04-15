package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entities.characters.*;
import entities.items.*;
import entities.spaces.Exit;
import entities.spaces.ExitDirection;
import entities.spaces.Hallway;
import entities.spaces.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static util.GameUtils.*;

public class GameGenerator {

    public static final int HEIGHT = 62;
    public static final int WIDTH = 140;
    public static final int MAX_LEVEL = 21;

    private Player player;
    private final List<Room> rooms;
    private final List<Hallway> hallways;
    private final List<Item> items;
    private final List<Monster> monsters;

    @JsonIgnore
    private Random rnd;
    private int difficultyLevel;

    public GameGenerator() {
        rooms = new ArrayList<>();
        hallways = new ArrayList<>();
        items = new ArrayList<>();
        monsters = new ArrayList<>();
        rnd = new Random();
    }

    public Player getPlayer() {
        return player;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Hallway> getHallways() {
        return hallways;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public Random getRnd() {
        return rnd;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void generateGameLevel() {
        difficultyLevel++;
        generateRooms();
        generateHallways();
        generateItems();
        generateMonsters();
    }

    public void resetDifficulty() {
        difficultyLevel = 0;
    }

    public void generatePlayer() {
        player = new Player(50, 15, 15, 1);
    }

    public char generateMonsterDirection() {
        int direction = rnd.nextInt(4);
        return switch (direction) {
            case 0 -> UP;
            case 1 -> DOWN;
            case 2 -> LEFT;
            case 3 -> RIGHT;
            default -> 0;
        };
    }

    private void generateRooms() {
        rooms.clear();
        int x, y, boundX = 6, minX = 5, boundY = 3, minY = 2;
        for (int i = 0; i < 9; i++) {
            if (i % 3 == 0) {
                x = 25 + rnd.nextInt(boundX) + minX;
                if (i != 0) {
                    boundY = 5;
                    minY += 19;
                }
            } else {
                x = rnd.nextInt(boundX) + rooms.get(i - 1).getX() + rooms.get(i - 1).getWidth() + minX * 4;
            }
            y = rnd.nextInt(boundY) + minY;
            Room room = new Room(x, y, rnd.nextInt(9) + 3, rnd.nextInt(9) + 2);
            generateExits(room, i + 1);
            rooms.add(room);
        }
        generateStartingRoom();
        generateEndingRoom();
        generatePlayerPosition();
    }

    private void generateExits(Room room, int number) {
        List<Exit> exits = new ArrayList<>();
        switch (number) {
            case 1:
                for (Integer i : generateNumberOfExits()) {
                    exits.add(createExit(room, i));
                }
                break;
            case 2:
                checkAndCreateExit(exits, room, 0, ExitDirection.EAST);
                for (Integer i : generateNumberOfExits()) {
                    exits.add(createExit(room, i));
                }
                break;
            case 3:
                checkAndCreateExit(exits, room, 1, ExitDirection.EAST);
                exits.add(createExit(room, ExitDirection.SOUTH.getValue()));
                break;
            case 4:
                checkAndCreateExit(exits, room, 0, ExitDirection.SOUTH);
                for (Integer i : generateNumberOfExits()) {
                    exits.add(createExit(room, i));
                }
                break;
            case 5:
                checkAndCreateExit(exits, room, 1, ExitDirection.SOUTH);
                checkAndCreateExit(exits, room, 3, ExitDirection.EAST);
                for (Integer i : generateNumberOfExits()) {
                    exits.add(createExit(room, i));
                }
                break;
            case 6:
                checkAndCreateExit(exits, room, 2, ExitDirection.SOUTH);
                checkAndCreateExit(exits, room, 4, ExitDirection.EAST);
                exits.add(createExit(room, ExitDirection.SOUTH.getValue()));
                break;
            case 7:
                checkAndCreateExit(exits, room, 3, ExitDirection.SOUTH);
                exits.add(createExit(room, ExitDirection.EAST.getValue()));
                break;
            case 8:
                checkAndCreateExit(exits, room, 4, ExitDirection.SOUTH);
                checkAndCreateExit(exits, room, 6, ExitDirection.EAST);
                exits.add(createExit(room, ExitDirection.EAST.getValue()));
                break;
            case 9:
                checkAndCreateExit(exits, room, 5, ExitDirection.SOUTH);
                checkAndCreateExit(exits, room, 7, ExitDirection.EAST);
                break;
        }
        room.setExits(exits);
    }

    private Set<Integer> generateNumberOfExits() {
        return rnd.ints(1, 3)
                .distinct()
                .limit(rnd.nextInt(2) + 1)
                .boxed()
                .collect(Collectors.toSet());
    }

    private void checkAndCreateExit(List<Exit> exits, Room room, int roomIndex, ExitDirection direction) {
        if (rooms.get(roomIndex).getExits()
                .stream()
                .anyMatch(exit -> exit.getDirection() == direction)) {
            exits.add(createExit(room, (direction.getValue() + 2) % 4));
        }
    }

    private Exit createExit(Room room, int direction) {
        Exit exit = new Exit(ExitDirection.getDirection(direction));
        int x, y;
        switch (exit.getDirection()) {
            case NORTH:
                x = rnd.nextInt(room.getWidth());
                exit.setCoordinates(x + room.getX(), room.getY() - 1);
                break;
            case EAST:
                y = rnd.nextInt(room.getHeight());
                exit.setCoordinates(room.getX() + room.getWidth(), y + room.getY());
                break;
            case SOUTH:
                x = rnd.nextInt(room.getWidth());
                exit.setCoordinates(x + room.getX(), room.getY() + room.getHeight());
                break;
            case WEST:
                y = rnd.nextInt(room.getHeight());
                exit.setCoordinates(room.getX() - 1, y + room.getY());
        }
        return exit;
    }

    private void generateStartingRoom() {
        int startIndex = rnd.nextInt(rooms.size());
        rooms.get(startIndex).setStarting(true);
    }

    private void generateEndingRoom() {
        int roomIndex;
        do {
            roomIndex = rnd.nextInt(rooms.size());
        } while (rooms.get(roomIndex).isStarting());
        Room room = rooms.get(roomIndex);
        int x, y;
        do {
            x = room.getX() + rnd.nextInt(room.getWidth());
            y = room.getY() + rnd.nextInt(room.getHeight());
        } while (checkTransition(room, x - 1, y) || checkTransition(room, x + 1, y)
                || checkTransition(room, x, y - 1) || checkTransition(room, x, y + 1));
        room.setTransition(new Point(x, y));
    }

    private boolean checkTransition(Room room, int x, int y) {
        return room.getExits()
                .stream()
                .anyMatch(exit -> exit.getX() == x && exit.getY() == y);
    }

    private void generatePlayerPosition() {
        for (Room room : rooms) {
            if (room.isStarting()) {
                room.setVisited(true);
                room.setCurrent(true);
                player.move(room.getX() + rnd.nextInt(room.getWidth()),
                        room.getY() + rnd.nextInt(room.getHeight()));
                break;
            }
        }
    }

    private void generateHallways() {
        hallways.clear();
        for (int i = 0; i < rooms.size(); i++) {
            for (Exit roomExit : rooms.get(i).getExits()) {
                if (roomExit.getDirection() == ExitDirection.NORTH || roomExit.getDirection() == ExitDirection.WEST) {
                    continue;
                }
                int secondRoomIndex = roomExit.getDirection() == ExitDirection.EAST ? 1 : 3;
                Exit secondRoomExit = rooms.get(i + secondRoomIndex).getExits()
                        .stream()
                        .filter(exit -> exit.getDirection() ==
                                ExitDirection.getDirection((roomExit.getDirection().getValue() + 2) % 4))
                        .findAny()
                        .orElseThrow(RuntimeException::new);
                Exit start = new Exit();
                Exit end = new Exit();
                start.setCoordinates(roomExit.getX(), roomExit.getY());
                end.setCoordinates(secondRoomExit.getX(), secondRoomExit.getY());
                hallways.add(createHallway(roomExit.getDirection(), start, end));
            }
        }
    }

    private Hallway createHallway(ExitDirection direction, Exit start, Exit end) {
        Hallway hallway = new Hallway();
        hallway.setDirection(direction);
        hallway.setStart(start);
        hallway.setEnd(end);
        int j = 0;
        if (hallway.getDirection() == ExitDirection.EAST) {
            for (; j < (end.getX() - start.getX()) / 2; j++) {
                hallway.getCoordinates().add(new Point(start.getX() + j, start.getY()));
            }
            if (start.getY() < end.getY()) {
                for (int i = start.getY(); i <= end.getY(); i++) {
                    hallway.getCoordinates().add(new Point(start.getX() + j, i));
                }
            } else if (start.getY() > end.getY()) {
                for (int i = start.getY(); i >= end.getY(); i--) {
                    hallway.getCoordinates().add(new Point(start.getX() + j, i));
                }
            }
            for (int i = start.getX() + j; i <= end.getX(); i++) {
                hallway.getCoordinates().add(new Point(i, end.getY()));
            }
        } else {
            for (; j < (end.getY() - start.getY()) / 2; j++) {
                hallway.getCoordinates().add(new Point(start.getX(), start.getY() + j));
            }
            if (start.getX() < end.getX()) {
                for (int i = start.getX(); i <= end.getX(); i++) {
                    hallway.getCoordinates().add(new Point(i, start.getY() + j));
                }
            } else if (start.getX() > end.getX()) {
                for (int i = start.getX(); i >= end.getX(); i--) {
                    hallway.getCoordinates().add(new Point(i, start.getY() + j));
                }
            }
            for (int i = start.getY() + j; i <= end.getY(); i++) {
                hallway.getCoordinates().add(new Point(end.getX(), i));
            }
        }
        return hallway;
    }

    private void generateItems() {
        items.clear();
        int maxNumberOfItems;
        if (difficultyLevel <= MAX_LEVEL / 2) {
            maxNumberOfItems = MAX_LEVEL / 2;
            for (int i = 0; i < maxNumberOfItems / 2; i++) {
                createItem(ItemType.FOOD);
                createItem(ItemType.ELIXIR);
            }
            for (int i = 0; i < maxNumberOfItems / 2; i++) {
                createItem(ItemType.GOLD);
            }
        } else {
            maxNumberOfItems = difficultyLevel < MAX_LEVEL - 1 ? MAX_LEVEL / 4 - (difficultyLevel % 10 - 1) : 0;
            for (int i = 0; i < maxNumberOfItems; i++) {
                createItem(ItemType.FOOD);
                createItem(ItemType.ELIXIR);
            }
            for (int i = 0; i < MAX_LEVEL / 2 - maxNumberOfItems; i++) {
                createItem(ItemType.GOLD);
            }
        }
        if (rnd.nextBoolean()) {
            createItem(ItemType.SCROLL);
        }
        if (rnd.nextBoolean()) {
            createItem(ItemType.WEAPON);
        }
    }

    private void createItem(ItemType type) {
        Item item = switch (type) {
            case GOLD -> new Gold(rnd.nextInt(11) + 10 + difficultyLevel);
            case FOOD -> new Food(rnd.nextInt(11) + 5 + difficultyLevel / 2);
            case ELIXIR -> new Elixir(rnd.nextInt(3), rnd.nextInt(6) + 10);
            case SCROLL -> new Scroll(rnd.nextInt(3), rnd.nextInt(6) + 3);
            case WEAPON -> new Weapon(rnd.nextInt(11) + difficultyLevel);
        };
        do {
            Room room = rooms.get(rnd.nextInt(rooms.size()));
            item.setCoordinates(room.getX() + rnd.nextInt(room.getWidth()),
                    room.getY() + rnd.nextInt(room.getHeight()));
        } while (checkItemCollision(item.getX(), item.getY()));
        items.add(item);
    }

    private boolean checkItemCollision(int x, int y) {
        Room endingRoom = rooms.stream()
                .filter(room -> room.getTransition() != null)
                .findAny()
                .orElseThrow(RuntimeException::new);
        return items.stream()
                .anyMatch(item -> item.getX() == x && item.getY() == y)
                || (player != null && player.getX() == x && player.getY() == y)
                || (endingRoom.getTransition().x == x && endingRoom.getTransition().y == y);
    }

    private void generateMonsters() {
        monsters.clear();
        if (difficultyLevel <= MAX_LEVEL / 2) {
            for (int i = 0; i < 3; i++) {
                if (rnd.nextBoolean()) {
                    createMonster(MonsterType.GHOUL);
                }
            }
        } else {
            if (difficultyLevel < 17) {
                for (int i = 0; i < 3; i++) {
                    if (rnd.nextBoolean()) {
                        createMonster(MonsterType.OGRE);
                    }
                    if (rnd.nextBoolean()) {
                        createMonster(MonsterType.SNAKE);
                    }
                }
            } else {
                for (int i = 16; i < difficultyLevel; i++) {
                    createMonster(MonsterType.OGRE);
                }
                if (rnd.nextBoolean()) {
                    createMonster(MonsterType.SNAKE);
                }
            }
        }
        if (rnd.nextBoolean()) {
            createMonster(MonsterType.VAMPIRE);
        }
        if (rnd.nextBoolean()) {
            createMonster(MonsterType.OGRE);
        }
        if (rnd.nextBoolean()) {
            createMonster(MonsterType.SNAKE);
        }
        for (int i = 0; i < 5; i++) {
            createMonster(MonsterType.ZOMBIE);
        }
    }

    private void createMonster(MonsterType type) {
        Monster monster = switch (type) {
            case ZOMBIE -> new Zombie(30 + difficultyLevel / 2, 10, 6 + difficultyLevel / 3,
                    2, 1);
            case VAMPIRE -> new Vampire(30 + difficultyLevel / 2, 20, 6 + difficultyLevel / 3,
                    3, 1);
            case GHOUL -> new Ghoul(10 + difficultyLevel / 2, 20, 3 + difficultyLevel / 3,
                    1, 1);
            case OGRE -> new Ogre(40 + difficultyLevel / 2, 10, 12 + difficultyLevel / 3,
                    2, 2);
            case SNAKE -> new Snake(20 + difficultyLevel / 2, 25, 9 + difficultyLevel / 3,
                    3, 1);
        };
        do {
            Room room;
            do {
                room = rooms.get(rnd.nextInt(rooms.size()));
            } while (room.isStarting());
            monster.move(room.getX() + rnd.nextInt(room.getWidth()),
                    room.getY() + rnd.nextInt(room.getHeight()));
        } while (checkItemCollision(monster.getX(), monster.getY())
                && checkMonsterCollision(monster.getX(), monster.getY()));
        monsters.add(monster);
    }

    private boolean checkMonsterCollision(int x, int y) {
        return monsters.stream()
                .anyMatch(monster -> monster.getX() == x && monster.getY() == y);
    }
}
package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.GameGenerator;
import domain.GameStatistics;

import java.io.*;
import java.util.List;

public class GameRepository {

    private static final String GAME_PATH = "src/main/resources/game_save.json";
    private static final String STATISTICS_PATH = "src/main/resources/statistics_save.json";
    private final ObjectMapper mapper;

    public GameRepository() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public void saveGame(GameGenerator generator) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_PATH))) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, generator);
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }

    public GameGenerator loadGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GAME_PATH))) {
            return mapper.readValue(reader, GameGenerator.class);
        } catch (IOException i) {
            return null;
        }
    }

    public void saveStatistics(List<GameStatistics> statistics) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STATISTICS_PATH))) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, statistics);
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }

    public List<GameStatistics> loadStatistics() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STATISTICS_PATH))) {
            return mapper.readValue(reader, new TypeReference<>() {
            });
        } catch (IOException i) {
            return null;
        }
    }

    public void clearGameSave() {
        try {
            new BufferedWriter(new FileWriter(GAME_PATH, false)).close();
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }
}

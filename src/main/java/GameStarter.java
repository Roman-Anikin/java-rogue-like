import controller.GameController;
import domain.GameService;
import presentation.GameRender;

public class GameStarter {

    public static void main(String[] args) {
        GameService service = new GameService();
        GameController controller = new GameController(service);
        GameRender render = new GameRender(controller);
        render.init();
    }
}
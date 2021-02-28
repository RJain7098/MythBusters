package views;

import gamefiles.Player;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import gamefiles.Door;
import javafx.stage.Stage;

public class RoomOne {
    private int width;
    private int height;
    private Player player;

    private Text nameDisplay;
    private Text weaponDisplay;
    private Text coinDisplay;

    private RoomOne() { }

    /**
     * Create Room One.
     * @param width width of room screen
     * @param height height of room screen
     * @param player player in room
     */
    public RoomOne(int width, int height, Player player) {
        this.player = player;
        this.width = width;
        this.height = height;
    }

    /**
     * Get scene for Room One Screen.
     * @return room one scene
     */
    public Scene getScene() {
        nameDisplay = new Text(110, 10, "Name: " + player.getName());
        nameDisplay.setStyle("-fx-font-size: 30;");
        weaponDisplay = new Text(210, 10, "Weapon: " + player.getWeapon().getName());
        weaponDisplay.setStyle("-fx-font-size: 30;");
        coinDisplay = new Text(310, 10, "Coins: " + player.getCoins());
        coinDisplay.setStyle("-fx-font-size: 30;");

        HBox displays = new HBox(50, nameDisplay, weaponDisplay, coinDisplay);
        displays.setLayoutX(10);
        displays.setLayoutY(20);

        Door exit1 = new Door(1100, 100);
        Door exit2 = new Door(1100, 300);
        Door exit3 = new Door(1100, 500);
        Door exit4 = new Door(1100, 700);

        Group board = new Group();
        board.getChildren().addAll(displays);
        board.getChildren().addAll(exit1.getGroup(),
                exit2.getGroup(), exit3.getGroup(), exit4.getGroup());
        Scene scene = new Scene(board, width, height, Color.PURPLE);
        return scene;
    }

    /**
     * Set necessary binding properties.  Currently placeholder.
     * @param stage stage to bind to
     */
    public void setBinds(Stage stage) {
        //PlaceHolder for binding widths.
    }

    /**
     * @return the weapon display
     */
    public Text getWeaponDisplay() {
        return weaponDisplay;
    }

    /**
     * @return the coin display
     */
    public Text getCoinDisplay() {
        return coinDisplay;
    }

    /**
     * @return the name display
     */
    public Text getNameDisplay() {
        return nameDisplay;
    }
}

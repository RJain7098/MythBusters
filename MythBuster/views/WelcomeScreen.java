package views;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class WelcomeScreen {
    private int width;
    private int height;
    private Button startButton;
    private Label header;

    private WelcomeScreen() { }

    /**
     * Set up welcome screen.
     * @param width width of screen
     * @param height height of screen
     */
    public WelcomeScreen(int width, int height) {
        this.width = width;
        this.height = height;
        startButton = new Button("Start Game!");

        header = new Label("MYTHBUSTERS");
        header.setStyle("-fx-font-size: 100; -fx-font-weight: bold;-fx-border-color:red;"
                + "-fx-alignment:CENTER; -fx-text-fill: #DEB887; -fx-background-color:black");
    }

    /**
     * Get welcome screen scene.
     * @return welcome screen scene
     */
    public Scene getScene() {
        Group board = new Group();

        ImageView leftImageView = new ImageView("sprites/Medusa.png");
        leftImageView.setFitWidth(200);
        leftImageView.setFitHeight(200);
        Node leftImage = leftImageView;
        leftImage.setLayoutX(100);
        leftImage.setLayoutY(250);

        ImageView rightImageView = new ImageView("sprites/Medusa.png");
        rightImageView.setFitWidth(200);
        rightImageView.setFitHeight(200);
        Node rightImage = rightImageView;
        rightImage.setLayoutX(width - 300);
        rightImage.setLayoutY(250);

        startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-weight: bold; -fx-font-size: 30");

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(startButton);
        vBox.setPrefWidth(width);
        vBox.setLayoutY(300);

        board.getChildren().addAll(header, leftImage, rightImage, vBox);
        Scene scene = new Scene(board, width, height, Color.PURPLE);

        return scene;
    }

    /**
     * Get start button.
     * @return start button
     */
    public Button getStartButton() {
        return startButton;
    }

    /**
     * Set up necessary binding properties.
     * @param stage stage to bind items in screen to
     */
    public void setBinds(Stage stage) {
        header.minWidthProperty().bind(stage.widthProperty());
    }
}
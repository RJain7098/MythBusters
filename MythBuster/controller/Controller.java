package controller;


import gamefiles.*;
import gamefiles.characters.Player;
import gamefiles.characters.Trap;
import gamefiles.items.ItemDatabase;
import gamefiles.rooms.Room;
import gamefiles.rooms.RoomLayout;
import gamefiles.weapons.Bow;
import gamefiles.weapons.WeaponDatabase;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import views.ConfigurationScreen;
import views.DeathScreen;
import views.GameScreen;
import views.WelcomeScreen;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sounds.BackgroundMusic;
import views.WinScreen;

import java.util.ArrayList;


public class Controller extends Application {
    private static Stage mainWindow;
    private static final int W = 1200;
    private static final int H = 800;
    private static Player player;
    private static GameScreen gameScreen;
    private static RoomLayout roomLayout;
    private static Room currentRoom;
    private static Room prevRoom;
    private static AnimationTimer controllerLoop;

    private static int gameDifficulty;

    public void start(Stage primaryStage) throws Exception {
        mainWindow = primaryStage;
        mainWindow.setTitle("MythBusters!");
        WeaponDatabase.initialize();
        ItemDatabase.initialize();
        BackgroundMusic.initialize();
        BackgroundMusic.getTrack().play();
        initWelcomeScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void initWelcomeScreen() {
        WelcomeScreen welcomeScreen = new WelcomeScreen(W, H);
        Scene scene = welcomeScreen.getScene();
        Button startGameButton = welcomeScreen.getStartButton();

        startGameButton.setOnAction(e -> {
            goToConfigurationScreen();
        });
        welcomeScreen.setBinds(mainWindow);

        mainWindow.setScene(scene);
        mainWindow.show();
    }

    public static void goToConfigurationScreen() {
        ConfigurationScreen configScreen = new ConfigurationScreen(W, H);
        Button beginButton = configScreen.getBeginButton();
        TextField heroNameField = configScreen.getHeroNameField();
        ComboBox<StartingWeapon> startingWeaponSelector = configScreen.getStartingWeaponSelector();
        ComboBox<Difficulty> difficultySelector = configScreen.getDifficultySelector();

        beginButton.addEventHandler(ActionEvent.ACTION, (e) -> {
            if (heroNameField.getText().length() < 1
                    || heroNameField.getText().trim().isEmpty()) {
                showAlert("Your name cannot be empty or whitespace only!");
                return;
            }
            setDifficulty(difficultySelector.getValue());
            initializeStats(heroNameField.getText(),
                    startingWeaponSelector.getSelectionModel().getSelectedIndex(),
                    difficultySelector.getValue());
            goToStartingRoom();
        });
        Scene scene = configScreen.getScene();
        mainWindow.setScene(scene);
        configScreen.setBinds(mainWindow);

    }

    public static void goToInventory() {
        Scene scene = Inventory.getScene();
        mainWindow.setScene(scene);
    }

    public static void goToStartingRoom() {
        //Initialize starting room.
        roomLayout = new RoomLayout();
        gameScreen = new GameScreen(W, H, player, roomLayout);
        currentRoom = roomLayout.getRoom(roomLayout.getStartRoomRow(),
                roomLayout.getStartRoomColumn());
        gameScreen.updateBoard(currentRoom);
        player.moveAbsolute(W / 2, H / 2);
        Scene scene = gameScreen.getScene();
        mainWindow.setScene(scene);
        playGame();
    }

    public static void playGame() {
        //Take in inputs
        ArrayList<String> input = new ArrayList<>();

        //Keyboard shortcuts related to the overall game go here
        gameScreen.getScene().setOnKeyReleased(
            e -> {
                String code = e.getCode().toString();
                input.add(code);
            });

        System.out.println("Test!");

        controllerLoop = new AnimationTimer() {
            public void handle(long currentNanoTime) {

                // game logic
                Group board = gameScreen.getBoard();
                HBox displays = gameScreen.getDisplays();

                // if there are no monsters, unlock the doors
                if (GameLoop.getMonsters().size() == 0) {
                    currentRoom.unlockDoors();
                }

                //If there is a left door and we are at it.
                if (currentRoom.getLeftDoor() != null
                            && player.intersects(currentRoom.getLeftDoor())
                            && !currentRoom.getLeftDoor().isLocked()) {
                    displays.getChildren().remove(currentRoom.getRoomInfo());
                    prevRoom = currentRoom;
                    currentRoom =
                            roomLayout.getRoom(currentRoom.getRow(), currentRoom.getColumn() - 1);
                    gameScreen.updateBoard(currentRoom);
                    for (int i = 0; i < 4; i++) {
                        Door d = currentRoom.getDoors()[i];
                        if (d != null) {
                            Room r = d.getDestination();
                            if ((r.getColumn() == prevRoom.getColumn())
                                && (r.getRow() == prevRoom.getRow())) {
                                d.unlock();
                            }
                        }
                    }
                    player.moveAbsolute(W - 200, H / 2 - player.getHeight() / 2);
                }

                //If there is a right door and we are at it.
                if (currentRoom.getRightDoor() != null
                            && player.intersects(currentRoom.getRightDoor())
                            && !currentRoom.getRightDoor().isLocked()) {
                    displays.getChildren().remove(currentRoom.getRoomInfo());
                    prevRoom = currentRoom;
                    currentRoom =
                            roomLayout.getRoom(currentRoom.getRow(), currentRoom.getColumn() + 1);
                    gameScreen.updateBoard(currentRoom);
                    for (int i = 0; i < 4; i++) {
                        Door d = currentRoom.getDoors()[i];
                        if (d != null) {
                            Room r = d.getDestination();
                            if ((r.getColumn() == prevRoom.getColumn())
                                && (r.getRow() == prevRoom.getRow())) {
                                d.unlock();
                            }
                        }
                    }
                    player.moveAbsolute(100, H / 2 - player.getHeight() / 2);
                }

                //If there is a top door and we are at it.
                if (currentRoom.getTopDoor() != null
                            && player.intersects(currentRoom.getTopDoor())
                            && !currentRoom.getTopDoor().isLocked()) {
                    displays.getChildren().remove(currentRoom.getRoomInfo());
                    prevRoom = currentRoom;
                    currentRoom =
                            roomLayout.getRoom(currentRoom.getRow() - 1, currentRoom.getColumn());
                    gameScreen.updateBoard(currentRoom);
                    for (int i = 0; i < 4; i++) {
                        Door d = currentRoom.getDoors()[i];
                        if (d != null) {
                            Room r = d.getDestination();
                            if ((r.getColumn() == prevRoom.getColumn())
                                && (r.getRow() == prevRoom.getRow())) {
                                d.unlock();
                            }
                        }
                    }
                    player.moveAbsolute(W / 2 - player.getWidth() / 2, H - 200);
                }

                //If there is a bottom door and we are at it.
                if (currentRoom.getBottomDoor() != null
                            && player.intersects(currentRoom.getBottomDoor())
                            && !currentRoom.getBottomDoor().isLocked()) {
                    displays.getChildren().remove(currentRoom.getRoomInfo());
                    prevRoom = currentRoom;
                    currentRoom =
                            roomLayout.getRoom(currentRoom.getRow() + 1, currentRoom.getColumn());
                    gameScreen.updateBoard(currentRoom);
                    for (int i = 0; i < 4; i++) {
                        Door d = currentRoom.getDoors()[i];
                        if (d != null) {
                            Room r = d.getDestination();
                            if ((r.getColumn() == prevRoom.getColumn())
                                && (r.getRow() == prevRoom.getRow())) {
                                d.unlock();
                            }
                        }
                    }
                    player.moveAbsolute(W / 2 - player.getWidth()/ 2, 100);
                }

            }
        };

        GameLoop.initializeAllAnimationTimers(player, gameScreen);
        if (player.getWeapon() instanceof Bow) {
            GameLoop.startAllAnimationTimers(player.getPlayerLogicTimer(),
                ((Bow) player.getWeapon()).getArrowTimer(),
                    player.getPlayerHpUpdateTimer(),
                    GameLoop.getMonsterLoop(), controllerLoop, player.getItemLoop());
        } else {
            GameLoop.startAllAnimationTimers(player.getPlayerLogicTimer(),
                    player.getPlayerHpUpdateTimer(),
                    GameLoop.getMonsterLoop(), controllerLoop, player.getItemLoop());
        }
    }

    public static void goToGameScreen() {
        Scene scene = gameScreen.getScene();
        mainWindow.setScene(scene);
    }

    public static void goToWinScreen() {
        WinScreen winScreen = new WinScreen(W, H);
        Scene scene = winScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    public static void goToBossRoom() {
        //Go to boss room(used for testing).
        currentRoom = roomLayout.getRoom(roomLayout.getBossRoomRow(),
                                        roomLayout.getBossRoomColumn());
        gameScreen.updateBoard(currentRoom);
        player.moveAbsolute(W / 2, H / 2);
    }

    public static void goToDeathScreen() {
        GameLoop.stopAllAnimationTimers(player.getPlayerLogicTimer(),
                player.getPlayerHpUpdateTimer(), GameLoop.getMonsterLoop(),
                controllerLoop, player.getItemLoop());
        Trap.setTrapCount(0);

        for (int i = 0; i < 5; i++) {
            Inventory.removeFromHotbar(i);
        }
        Inventory.setHotbarSize(0);
        Inventory.clearInventory();

        DeathScreen deathScreen = new DeathScreen(W, H);
        player = new Player(0, null);

        Button restartButton = deathScreen.getRestartButton();
        restartButton.addEventHandler(ActionEvent.ACTION, (e) -> {
            goToConfigurationScreen();
        });

        Scene scene = deathScreen.getScene();
        mainWindow.setScene(scene);
        mainWindow.show();
    }

    private static void setDifficulty(Difficulty difficulty) {
        switch (difficulty) {
        case EASY:
            gameDifficulty = 0;
            break;
        case MEDIUM:
            gameDifficulty = 1;
            break;
        case HARD:
            gameDifficulty = 2;
            break;
        default:
            break;
        }
    }

    public static int getDifficulty() {
        return gameDifficulty;
    }

    /**
     * Set initial parameters
     *
     * @param nameEntry           the name of the hero
     * @param startingWeaponIndex the index of the starting weapon
     * @param difficultyEntry     the difficulty
     */
    private static void initializeStats(String nameEntry,
                                 int startingWeaponIndex, Difficulty difficultyEntry) {
        player = new Player(0, WeaponDatabase.getWeapon(startingWeaponIndex));
        player.setName(nameEntry);
        switch (difficultyEntry) {
        case EASY:
            player.setCoins(30);
            break;
        case MEDIUM:
            player.setCoins(20);
            break;
        case HARD:
            player.setCoins(10);
            break;
        default: // unnecessary because of type safety
        }
    }

    /**
     * Alert Method
     * @param message Message to display in alert.
     */
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.showAndWait();
        return;
    }

    /**
     * @return the player object
     */
    public static Player getPlayer() {
        return player;
    }

    public static views.GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Returns the current room
     * @return the current room
     */
    public static Room getCurrentRoom() {
        return currentRoom;
    }
    /**
     * @return the first room
     */
    public GameScreen getRoomOne() {
        return gameScreen;
    }

    public RoomLayout getRoomLayout() {
        return roomLayout;
    }

    public static AnimationTimer getControllerLoop() {
        return controllerLoop;
    }

    /**
     * Private testing method to return a String representation of the Label of the window.
     * @return the string representing the label of the window.
     */
    public String getWindowTitle() {
        return mainWindow.getTitle();
    }

    /**
     * Getter for the width of the map.
     * @return an int representing the width of the map
     */
    public static int getW() {
        return W;
    }

    /**
     * Getter for the height of the map.
     * @return an int represneting the height of the map.
     */
    public static int getH() {
        return H;
    }




}

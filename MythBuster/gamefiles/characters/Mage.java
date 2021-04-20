package gamefiles.characters;

import controller.Controller;
import controller.GameLoop;
import javafx.application.Platform;

public class Mage extends Monster {

    public Mage() {
        super("Mage Enemy", 100, 5, "sprites/FF1Mage.png", 100, 100);
    }

    public Mage(double health, double movementSpeed, String spritePath, 
                double width, double height) {
        super("Mage Enemy", health, movementSpeed, spritePath, width, height);

    }

    private int spawnFireballCooldown = 15;
    private double targetPositionX = Math.random() * (Controller.getW() - width);
    private double targetPositionY = Math.random() * (Controller.getH() - height);


    public void update() {
        // game logic
        checkDeath();
        redrawHealthBar();
        // move
        double offsetX = targetPositionX - positionX;
        double offsetY = targetPositionY - positionY;
        double magnitude = Math.sqrt(offsetX * offsetX + offsetY * offsetY);
        if (Math.abs(offsetX) > 10 || Math.abs(offsetY) > 10) {
            moveRelative(movementSpeed * offsetX / magnitude, movementSpeed * offsetY / magnitude);
        } else {
            if (spawnFireballCooldown > 0) {
                spawnFireballCooldown--;
            } else {
                //Shoot a fireball.

                Fireball fireball = new Fireball(positionX, positionY);
                Platform.runLater(() -> {
                    Controller.getGameScreen().getBoard().getChildren().add(fireball.getGroup());
                });
                GameLoop.getMonsters().add(fireball);

                spawnFireballCooldown = 500;
                targetPositionX = Math.random() * (Controller.getW() - width);
                targetPositionY = Math.random() * (Controller.getH() - height);

            }
        }
    }
}

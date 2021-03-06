package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.view.modelview.ModelView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class YouLostSceneController extends GUIController {

    /* ===== FXML elements ===== */
    @FXML
    private ImageView looseImageView;
    @FXML
    private Label youLostLabel;

    /* ===== FXML Set Up and Bindings ===== */
    @FXML
    public void initialize() {

    }

    /**
     * Sets up the scene setting the text of the youLostLabel and loading the correct looseImageView image.
     * @param winnerName is the name of the winner.
     */
    public void loadData(String winnerName) {
        youLostLabel.setText("You lost. The winner is " + winnerName);

        Image looseImage = new Image("images/utility/loose.png");
        looseImageView.setImage(looseImage);
        looseImageView.setPreserveRatio(true);
    }

    /**
     * It is activated when the player clicks on the close button.
     * It closes the current stage.
     */
    public void closeStage() {
        stage.close();
    }
}

package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUIController;
import it.polimi.ingsw.client.gui.GUIEngine;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.utility.messages.sets.*;
import it.polimi.ingsw.view.modelview.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainSceneController extends GUIController {

    /* ===== Constants ===== */
    private static final double BOARD_PADDING_RATIO = 0.74;
    private static final double BOARD_PADDING_PERCENTAGE = 0.13;
    private static final int BOARD_SIZE = 5;
    private static final double CLIENT_CARD_WIDTH_RATIO = 6;
    private static final double CLIENT_CARD_HEIGHT_RATIO = 2.5;
    private static final double ENEMY_CARD_WIDTH_RATIO = 12;
    private static final double ENEMY_CARD_HEIGHT_RATIO = 5;
    private static final double BOARD_PANE_RATIO = 0.5;
    private static final int TIME_TO_WAIT = 5;


    /* ===== FXML elements ===== */
    @FXML
    private Label phaseLabel;
    @FXML
    private HBox blockTypesHBox;
    @FXML
    private GridPane mainGridPane;

    @FXML
    private GridPane boardGridPane;
    @FXML
    private AnchorPane boardAnchorPane;

    @FXML
    private Label clientPlayerNameLabel;
    @FXML
    private Label enemy1PlayerNameLabel;
    @FXML
    private Label enemy2PlayerNameLabel;

    @FXML
    private ImageView clientPlayerCardImageView;
    @FXML
    private ImageView enemy1PlayerCardImageView;
    @FXML
    private ImageView enemy2PlayerCardImageView;

    @FXML
    private Button skipButton;

    @FXML
    private ImageView timerImageView;
    @FXML
    private Button confirmActionButton;
    @FXML
    private Button undoActionButton;
    @FXML
    private Button undoTurnButton;



    /* ===== FXML Properties ===== */
    private DoubleProperty boardPaddingPercentage = new SimpleDoubleProperty(BOARD_PADDING_PERCENTAGE);

    /* ===== Variables ===== */
    private ImageView[][] enlightenedImageViewsArray = new ImageView[BOARD_SIZE][BOARD_SIZE];
    private ArrayList<Position> initialPawnPositionsList = new ArrayList<>();
    private Position chosenPosition = new Position(0,0);
    private BlockType chosenBlockType = null;
    private Timer undoProcessTimer;

    /* ===== FXML Set Up and Bindings ===== */
   @FXML
    public void initialize() {
       phaseLabel.setText("");
       clientPlayerNameLabel.setText("");
       enemy1PlayerNameLabel.setText("");
       enemy2PlayerNameLabel.setText("");
       skipButton.setVisible(false);
       timerImageView.setVisible(false);
       timerImageView.setPreserveRatio(true);
       confirmActionButton.setVisible(false);
       undoActionButton.setVisible(false);
       undoTurnButton.setVisible(false);

       //player cards dimensions bindings
       clientPlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(CLIENT_CARD_WIDTH_RATIO));
       clientPlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(CLIENT_CARD_HEIGHT_RATIO));
       enemy1PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(ENEMY_CARD_WIDTH_RATIO));
       enemy1PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(ENEMY_CARD_HEIGHT_RATIO));
       enemy2PlayerCardImageView.fitWidthProperty().bind(mainGridPane.widthProperty().divide(ENEMY_CARD_WIDTH_RATIO));
       enemy2PlayerCardImageView.fitHeightProperty().bind(mainGridPane.heightProperty().divide(ENEMY_CARD_HEIGHT_RATIO));

       //board dimensions bindings
       boardAnchorPane.setPrefWidth(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO).getValue());
       boardAnchorPane.setPrefHeight(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO).getValue());
       boardAnchorPane.maxWidthProperty().bind(mainGridPane.heightProperty().multiply(BOARD_PANE_RATIO));
       boardAnchorPane.maxHeightProperty().bind(boardAnchorPane.widthProperty());

       //TODO: improve padding management
       boardGridPane.paddingProperty().bind((Bindings.createObjectBinding(() -> new Insets(boardGridPane.widthProperty().multiply(BOARD_PADDING_PERCENTAGE).doubleValue()), boardGridPane.widthProperty().multiply(BOARD_PADDING_PERCENTAGE))));
       System.out.println(boardGridPane.paddingProperty());

       //binds HBox (used for BlockType selection) padding and internal spacing
       blockTypesHBox.spacingProperty().bind(blockTypesHBox.widthProperty().divide(10));
       //TODO: add padding to the blockType selection HBox
//       blockTypesHBox.paddingProperty().bind((Bindings.createObjectBinding(() -> new Insets(blockTypesHBox.widthProperty().divide(10).doubleValue()), blockTypesHBox.widthProperty().divide(10))));
   }


   public void buildMainScene() {
       updatePlayersName();
       updatePlayersCard();
       updateBoard();
       loadEnlightenedImageViews();
   }

   //TODO: remove this method and the relative button. It is just for test
   public void updateBoardTest() {
       ArrayList<Position> availablePositions = new ArrayList<>();
       availablePositions.add(new Position(1,1));
       availablePositions.add(new Position(1,2));
       clientView.getUserInterface().onInitialPawnPositionRequest(availablePositions);
   }

   public void updateGameInfo() {
       updatePlayersName();
       updatePlayersCard();
   }


    /**
     * clears the boardGridPane and renders the updated board from the modelView loading first the block types imageViews
     * and then the pawns imageViews
     */
   public void updateBoard() {
       //TODO: there may be a way to avoid re-loading the ImageViews within enlightenedImageViewsArray
       boardGridPane.getChildren().clear();
       ModelView modelView = clientView.getModelView();
       for(int i = 0; i < modelView.getMatrix().length; i++){
           for(int  j = 0; j < modelView.getMatrix()[0].length; j++){
               if (modelView.getMatrix()[i][j].getPeek() != BlockType.TERRAIN) {
                   System.out.println("blockType: " + modelView.getMatrix()[i][j].getPeek().getLevel());
                   Image blockImage = new Image("images/board/block_lv_" + modelView.getMatrix()[i][j].getPeek().getLevel() +".png");
                   ImageView blockImageView = new ImageView(blockImage);
                   blockImageView.setPreserveRatio(true);
                   blockImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
                   blockImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
                   boardGridPane.add(blockImageView, j, i);
               }
           }
       }
       updatePawns();
       loadEnlightenedImageViews();
   }

   private void updatePawns() {
       ModelView modelView = clientView.getModelView();
       ArrayList<PawnView> pawnsList = modelView.getPawns();
       for (PawnView pawn: pawnsList) {
           if (pawn.getPawnPosition() != null) {
               Image pawnImage = new Image("images/board/pawn_" + pawn.getColor() + ".png");
               ImageView pawnImageView = new ImageView(pawnImage);
               pawnImageView.setPreserveRatio(true);
               pawnImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
               pawnImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
               boardGridPane.add(pawnImageView, pawn.getPawnPosition().getY(), pawn.getPawnPosition().getX());
           }
       }

   }

   public void updatePlayersName() {
       ModelView modelView = clientView.getModelView();
       clientPlayerNameLabel.setText(clientView.getName());
       ArrayList<String> enemiesNames = modelView.getEnemiesNames(clientView.getName());
       if (enemiesNames != null) {
           if (enemiesNames.size() >= 1) enemy1PlayerNameLabel.setText(enemiesNames.get(0));
           if (enemiesNames.size() == 2) enemy2PlayerNameLabel.setText(enemiesNames.get(1));
       }
   }

   public void updatePlayersCard() {
       ModelView modelView = clientView.getModelView();

       //gets the Card of the client player from the modelView and renders the proper Image, Name and Description
       CardView clientPlayerCard = modelView.getClientPlayerCard(clientView.getName());
       if (clientPlayerCard != null) {
           Image clientPlayerCardImage = new Image("images/cards/card_" + clientPlayerCard.getId() + ".png");
           clientPlayerCardImageView.setImage(clientPlayerCardImage);
       }
       ArrayList<CardView> enemiesCards = modelView.getEnemiesCards(clientView.getName());
       if (enemiesCards != null) {
           if (enemiesCards.size() >= 1 && enemiesCards.get(0) != null)  {
               Image enemy1CardImage = new Image("images/cards/card_" + enemiesCards.get(0).getId() + ".png");
               enemy1PlayerCardImageView.setImage(enemy1CardImage); //enemiesCards.get(0)
           }
           if (enemiesCards.size() == 2 && enemiesCards.get(0) != null && enemiesCards.get(1) != null) {
               Image enemy2CardImage = new Image("images/cards/card_" + enemiesCards.get(1).getId() + ".png");
               enemy2PlayerCardImageView.setImage(enemy2CardImage);
           }
       }
   }

   private void loadEnlightenedImageViews() {
       ModelView modelView = clientView.getModelView();
       String playerColor = modelView.getPlayerColor(clientView.getName());
       //loads the panes that will be used to enlighten the board cells
       for (int i = 0; i < BOARD_SIZE; i++) {
           for (int j = 0; j < BOARD_SIZE; j++) {
               Image enlightenedImage = new Image("images/board/enlightened_cell_" + playerColor +".png");
               ImageView enlightenedImageView = new ImageView(enlightenedImage);
               enlightenedImageView.setOpacity(0.7);
               enlightenedImageViewsArray[i][j] = enlightenedImageView;
               enlightenedImageView.setId("permanent");
               enlightenedImageView.setPreserveRatio(true);
               enlightenedImageView.setVisible(false);
               enlightenedImageView.fitWidthProperty().bind(boardGridPane.widthProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
               enlightenedImageView.fitHeightProperty().bind(boardGridPane.heightProperty().divide(BOARD_SIZE).multiply(BOARD_PADDING_RATIO));
               boardGridPane.add(enlightenedImageView, j, i);
           }
       }
   }


   public void enablePawnSelection(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Select one of your pawns!");
       for (Position position : availablePositions) {
           System.out.println("X:" +  position.getX() + ", Y:" +  position.getY());
           //makes the ImageViews visible
           enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
           enlightenedImageViewsArray[position.getX()][position.getY()].toFront();
           //adds an action recognizer to the ImageView
           enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
               Node source = (Node)e.getSource();
               int colIndex = GridPane.getColumnIndex(source);
               int rowIndex = GridPane.getRowIndex(source);
               //cols -> x, rows -> y
               chosenPawn(new Position(rowIndex, colIndex));
           });
       }
   }

   private void chosenPawn(Position chosenPawnPosition) {
       phaseLabel.setText("");
       System.out.println("chosenPawnPosition:" + chosenPawnPosition.getX() + " " + chosenPawnPosition.getY());
       clientView.update(new SelectedPawnSetMessage(chosenPawnPosition));
       clearEnlightenedImageViews();
   }

    private void clearEnlightenedImageViews() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                enlightenedImageViewsArray[i][j].setVisible(false);
                enlightenedImageViewsArray[i][j].setOnMouseClicked(null);
            }
        }
    }

    public void chooseMovePosition(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Choose a position to Move!");
       if (availablePositions.contains(null)) skipButton.setVisible(true);
       enablePositionSelection(availablePositions, "move");
    }

    public void chooseConstructPosition(ArrayList<Position> availablePositions) {
       phaseLabel.setText("Choose a position to Build!");
       if (availablePositions.contains(null)) skipButton.setVisible(true);
       enablePositionSelection(availablePositions, "construct");
    }

    public void enablePositionSelection(ArrayList<Position> availablePositions, String actionType) {
        for (Position position : availablePositions) {
            //makes the ImageViews visible
            enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
            //adds an action recognizer to the ImageView
            enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int colIndex = GridPane.getColumnIndex(source);
                int rowIndex = GridPane.getRowIndex(source);

                chosenPosition.setX(rowIndex);
                chosenPosition.setY(colIndex);

                if (actionType.equals("move")) {
                    startUndoProcess();
                } else {
                    setPosition();
                }
            });
        }
    }

    private void setPosition() {
        phaseLabel.setText("");
        if (chosenPosition != null) {
            System.out.println("chosenPosition:" + chosenPosition.getX() + " " + chosenPosition.getY());
        } else {
            System.out.println("Action Skipped");
        }
        clientView.update(new ChosenPositionSetMessage(chosenPosition));
        clearEnlightenedImageViews();

        skipButton.setVisible(false);
    }

    public void chooseBlockType(ArrayList<BlockType> availableBlockTypes) {
        phaseLabel.setText("Choose a Block Type!");
        blockTypesHBox.toFront();

        for (BlockType blockType : availableBlockTypes) {
            Image blockTypeImage = new Image("images/board/block_lv_" + blockType.getLevel() + ".png");
            ImageView blockTypeImageView = new ImageView(blockTypeImage);
            blockTypeImageView.setPreserveRatio(true);
            blockTypeImageView.setId(String.valueOf(blockType.getLevel()));
            blockTypeImageView.fitWidthProperty().bind(blockTypesHBox.widthProperty().divide(availableBlockTypes.size()));
            blockTypeImageView.fitHeightProperty().bind(blockTypesHBox.heightProperty());
            blockTypesHBox.getChildren().add(blockTypeImageView);
            blockTypeImageView.setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int blockTypeLevel = Integer.parseInt(source.getId());
                System.out.printf("blockType: %d %n", blockTypeLevel);

                chosenBlockType = BlockType.LEVEL1;
                for(BlockType blockTypeElement : availableBlockTypes) {
                    if (blockType.getLevel() == blockTypeLevel) chosenBlockType = blockTypeElement;
                }

                blockTypesHBox.getChildren().clear();
                blockTypesHBox.setVisible(false);
                blockTypesHBox.toBack();
                startUndoProcess();
            });
        }

        blockTypesHBox.setVisible(true);
    }

    private void setBlockType() {
       phaseLabel.setText("");

       System.out.println("chosenBlockType.getLevel(): " + chosenBlockType.getLevel());
       clientView.update(new ChosenBlockTypeSetMessage(chosenBlockType));
       chosenBlockType = null;
    }

    public void placeInitialPawns(ArrayList<Position> availablePositions) {
        phaseLabel.setText("Place your 2 pawns!");
        //TODO: questa parte di codice è condivisa con enablePositionSelection. Potrei scrivere un metodo a cui si passa la funzione da attivare con l'action.
        for (Position position : availablePositions) {
            //makes the ImageViews visible
            enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(true);
            //adds an action recognizer to the ImageView
            enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(e -> {
                Node source = (Node)e.getSource();
                int colIndex = GridPane.getColumnIndex(source);
                int rowIndex = GridPane.getRowIndex(source);
                //cols -> x, rows -> y
                selectInitialPawnPosition(new Position(rowIndex, colIndex));
            });
        }
    }

    private void selectInitialPawnPosition(Position position) {
       ModelView modelView = clientView.getModelView();
       ArrayList<Integer> pawnsId = modelView.getPlayerPawnsId(clientView.getName());

       initialPawnPositionsList.add(position);
       if (initialPawnPositionsList.size() == 2) {
           phaseLabel.setText("");
           clearEnlightenedImageViews();
           System.out.println("firstPawnPosition1:" + initialPawnPositionsList.get(0).getX() + " " + initialPawnPositionsList.get(0).getY());
           System.out.println("firstPawnPosition2:" + initialPawnPositionsList.get(1).getX() + " " + initialPawnPositionsList.get(1).getY());
           clientView.update(new InitialPawnPositionSetMessage(pawnsId.get(0), pawnsId.get(1), initialPawnPositionsList.get(0), initialPawnPositionsList.get(1)));
       } else {
           phaseLabel.setText("Place the pawn left!");
           enlightenedImageViewsArray[position.getX()][position.getY()].setVisible(false);
           enlightenedImageViewsArray[position.getX()][position.getY()].setOnMouseClicked(null);
       }
    }

    public void skipAction() {
        phaseLabel.setText("");
        skipButton.setVisible(false);
        chosenPosition = null;
        clearEnlightenedImageViews();
    }

    private void startUndoProcess() {
        Image timerImage = new Image("images/utility/timer_counter_5.png");
        timerImageView.setImage(timerImage);

        timerImageView.setVisible(true);
        confirmActionButton.setVisible(true);
        undoActionButton.setVisible(true);
        undoTurnButton.setVisible(true);

        undoProcessTimer  = new Timer();

        undoProcessTimer.schedule( new TimerTask() {
            private int count = 5;
            @Override
            public void run() {
                if (count > 0) {
                    System.out.println("count: " + count);
                    Platform.runLater(() -> {

                        Image updatedTimerImage =  new Image("images/utility/timer_counter_" + count + ".png");
                        timerImageView.setImage(updatedTimerImage);
                        count--;
                    });
                } else {
                    Platform.runLater(() -> {
                        confirmAction();
                        undoProcessTimer.cancel();
                    });
                }
            }
        }, 0, 1000);
    }

    private void endUndoProcess() {
        undoProcessTimer.cancel();
        timerImageView.setVisible(false);
        confirmActionButton.setVisible(false);
        undoActionButton.setVisible(false);
        undoTurnButton.setVisible(false);
    }


    public void confirmAction() {
       if (chosenBlockType != null) {
           setBlockType();
       } else {
           setPosition();
       }
        clearEnlightenedImageViews();
        endUndoProcess();
    }

    public void undoAction() {
       clientView.update(new UndoActionSetMessage());
       endUndoProcess();
    }

    public void undoTurn() {
       clientView.update(new UndoTurnSetMessage());
       endUndoProcess();
    }

}

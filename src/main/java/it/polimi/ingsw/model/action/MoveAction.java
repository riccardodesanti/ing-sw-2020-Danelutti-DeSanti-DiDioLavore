package it.polimi.ingsw.model.action;

import java.lang.Math;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.board.BlockType;
import it.polimi.ingsw.model.board.Cell;

import java.util.ArrayList;
import java.util.Objects;


public class MoveAction extends Action {
    private Boolean moveUpEnable;
    private final Boolean swapEnable;
    private final Boolean moveOnOpponentEnable;
    private final Boolean pushEnable;
    private final Boolean denyMoveUpEnable;
    private final Boolean winDownEnable;
    private final ArrayList<Position> addMoveIfOn;
    private final Boolean denyMoveBack;
    private Boolean noWinIfOnPerimeter;



                                                                        //CONSTRUCTORS

    /**
     Constructor of MoveAction: calls the constructor of the superclass and sets the other parameters
     */
    public MoveAction(Boolean isOptional, ArrayList<Position> notAvailableCell, Boolean moveUpEnable, Boolean swapEnable, Boolean moveOnOpponentEnable, Boolean pushEnable, Boolean denyMoveUpEnable, Boolean winDownEnable, ArrayList<Position> addMoveIfOn, Boolean denyMoveBack, Boolean noWinIfOnPerimeter) {
        super(isOptional, notAvailableCell);
        this.moveUpEnable = moveUpEnable;
        this.swapEnable = swapEnable;
        this.moveOnOpponentEnable = moveOnOpponentEnable;
        this.pushEnable = pushEnable;
        this.denyMoveUpEnable = denyMoveUpEnable;
        this.winDownEnable = winDownEnable;
        if (addMoveIfOn != null) {
            this.addMoveIfOn = new ArrayList<>(addMoveIfOn);
        } else {
            this.addMoveIfOn = null;
        }
        this.denyMoveBack = denyMoveBack;
        this.noWinIfOnPerimeter = noWinIfOnPerimeter;
    }
    /**
     * This function is the copy constructor for the class MoveAction
     * By using this method, there is no need to implement Clonable
     */
    MoveAction(MoveAction toBeCopied) {
        super(toBeCopied.isOptional, toBeCopied.notAvailableCell, toBeCopied.selectedPawn, toBeCopied.notSelectedPawn);
        this.moveUpEnable = toBeCopied.moveUpEnable;
        this.swapEnable = toBeCopied.swapEnable;
        this.moveOnOpponentEnable = toBeCopied.moveOnOpponentEnable;
        this.pushEnable = toBeCopied.pushEnable;
        this.denyMoveUpEnable = toBeCopied.denyMoveUpEnable;
        this.winDownEnable = toBeCopied.winDownEnable;
        if (toBeCopied.addMoveIfOn != null) {
            this.addMoveIfOn=new ArrayList<>();
            for(Position p : toBeCopied.addMoveIfOn){
                this.addMoveIfOn.add(new Position(p));
            }
        } else {
            this.addMoveIfOn = null;
        }
        this.denyMoveBack = toBeCopied.denyMoveBack;
        this.noWinIfOnPerimeter = toBeCopied.noWinIfOnPerimeter;
    }
    /**
     * This function creates a duplicate of this.
     * @return MoveAction
     */
    public MoveAction duplicate() {
        return new MoveAction(this);
    }




                                                 //VISITOR PATTERN FUNCTIONS

    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and execute the current action without knowing the class type
     * @param visitor the visitor
     */
    public void acceptForExecution(ActionVisitor visitor){
        visitor.executeAction(this);
    }
    /**
     * This is the function needed to implement the visitor pattern for GameLogicExecutor and process the current action without knowing the class type
     * @param actionVisitor the visitor
     */
    public void acceptForProcess(ActionVisitor actionVisitor){
        actionVisitor.processAction(this);
    }



                                                    //SPECIFIC FUNCTIONS

    /**
     * Computes the list of cells to which a pawn can move
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells to which the pawn selected can move
     */
    @Override public ArrayList<Position> availableCells(Cell[][] matrixCopy) {
        ArrayList<Position> availableCells = new ArrayList<>();
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j=0; j<matrixCopy[0].length; j++) {
//              Adds to availableCells the cells adjacent to the selectedPawn
                if (!(selectedPawnPosition.getX() == i && selectedPawnPosition.getY() == j) && Math.abs(selectedPawnPosition.getX() - i) <= 1  && Math.abs(selectedPawnPosition.getY() - j) <= 1 ) {
                    availableCells.add(new Position(i, j));
                }
            }
        }
        checkNotAvailableCells(availableCells);
        checkDomePresence(availableCells, matrixCopy);
        checkDeltaHeight(availableCells, matrixCopy);
        checkPawnPresence(availableCells, matrixCopy);
        checkDenyMoveBack(availableCells);
        return availableCells;
    }
    /**
     * Checks whether the last moveAction executed upon the selectedPawn makes the player win. This can happen in two cases:
     * (1)the pawn has reached a level3 cell or (2) winDownEnable = true and the pawn went down at least 2 levels during the moveAction.
     * @param matrixCopy is a copy of the matrix within board
     * @return the list of available cells to which the pawn selected can move
     */
    public Boolean checkWin(Cell[][] matrixCopy) {
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        if (noWinIfOnPerimeter && isOnPerimeter()) return false;
        if (matrixCopy[selectedPawnPosition.getX()][selectedPawnPosition.getY()].peekBlock() == BlockType.LEVEL3 && selectedPawn.getDeltaHeight() > 0) return true;
        return winDownEnable && selectedPawn.getDeltaHeight() <= -2;
    }

    /**
     * This function returns the available block types for the current action
     * @param selectedPosition the position the selected position
     * @param matrixCopy the matrix of cells
     * @return the list of blocktypes
     */
    public ArrayList<BlockType> availableBlockTypes(Position selectedPosition, Cell[][] matrixCopy){
        return new ArrayList<>();
    }




                                                        //SETTERS

    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
    }
    public void disablePerimeterWin() {
        this.noWinIfOnPerimeter=true;
    }
    public void disableClimbing() {
        this.moveUpEnable=false;
    }
    public void blockSelected(BlockType blockType) {
    }




                                                         //GETTERS

    public Boolean getMoveUpEnable() {
        return moveUpEnable;
    }
    public Boolean getSwapEnable() {
        return swapEnable;
    }
    public Boolean getPushEnable() {
        return pushEnable;
    }
    public Boolean getDenyMoveUpEnable() {
        return denyMoveUpEnable;
    }
    public ArrayList<Position> getAddMoveIfOn() {
        return addMoveIfOn;
    }
    public Boolean getNoWinIfOnPerimeter() { return noWinIfOnPerimeter; }




                                                    //COMMODITY FUNCTIONS

    /**
     * Computes a boolean value expressing whether the pawn can move on a cell occupied by an enemy pawn
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     * @return the list of available cells to which the pawn selected can move
     */
    private Boolean canMoveOnOpponent(Cell[][] matrixCopy, Position selectedPawnPosition, Position enemyPawnPosition) {
        if (!swapEnable && !pushEnable) return false;
        if (swapEnable) return true;
        //relativePosition expresses the position of enemyPawn wrt the position of selectedPawn: its coordinates can value '0', '1' or '-1'
        Position relativePosition = new Position(enemyPawnPosition.getX() - selectedPawnPosition.getX(), enemyPawnPosition.getY() - selectedPawnPosition.getY());
        //relativePosition times 2 expresses the relative position wrt the position of selectedPawn that has to be checked. The absolute position that has to be checked is obtained by adding the relative position to the selectedPawnPosition
        Position positionToCheck = new Position(selectedPawnPosition.getX() + 2 * relativePosition.getX(), selectedPawnPosition.getY() + 2 * relativePosition.getY());
        if (positionToCheck.getX() < 0 || positionToCheck.getX() > 4 || positionToCheck.getY() < 0 || positionToCheck.getY() > 4) return false;
        Cell cellToCheck = matrixCopy[positionToCheck.getX()][positionToCheck.getY()];
        return cellToCheck.peekBlock() != BlockType.DOME && cellToCheck.getPawn() == null;
    }
    /**
     * Removes from availableCells the positions present within notAvailableCell
     * @param availableCells is the current list of cells where the selectedPawn can move
     */
    private void checkNotAvailableCells(ArrayList<Position> availableCells) {
        for (Position position : notAvailableCell) {
            availableCells.remove(position);
        }
    }
    /**
     * Removes from availableCells the positions where a dome is placed
     * @param availableCells is the current list of cells where the selectedPawn can move
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private void checkDomePresence(ArrayList<Position> availableCells, Cell[][] matrixCopy) {
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j = 0; j < matrixCopy[0].length; j++) {
                if (availableCells.contains(new Position(i, j))) {
                    if (matrixCopy[i][j].peekBlock() == BlockType.DOME) availableCells.remove(new Position(i, j));
                }
            }
        }
    }
    /**
     * Removes from availableCells the positions where the selectedPawn can't move because of an height threshold
     * @param availableCells is the current list of cells where the selectedPawn can move
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private void checkDeltaHeight(ArrayList<Position> availableCells, Cell[][] matrixCopy) {
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j = 0; j < matrixCopy[0].length; j++) {
                if (availableCells.contains(new Position(i, j))) {
                    int signedDeltaHeight = matrixCopy[i][j].getSize() - matrixCopy[selectedPawn.getPosition().getX()][selectedPawn.getPosition().getY()].getSize();
                    if (signedDeltaHeight > 1 || (signedDeltaHeight == 1 && !moveUpEnable)) availableCells.remove(new Position(i, j));
                }
            }
        }
    }
    /**
     * Removes from availableCells the position where selectedPawn can't move because of the presence of another pawn
     * @param availableCells is the current list of cells where the selectedPawn can move
     * @param matrixCopy is a copy of the matrix within board selectedPawnPosition
     */
    private void checkPawnPresence(ArrayList<Position> availableCells, Cell[][] matrixCopy) {
        for (int i=0; i<matrixCopy.length; i++) {
            for (int j = 0; j < matrixCopy[0].length; j++) {
                if (availableCells.contains(new Position(i, j))) {
                    if (notSelectedPawn != null) {
                        if (matrixCopy[i][j].getPawn() != null && (matrixCopy[i][j].getPawn().getPosition().equals(notSelectedPawn.getPosition()) || !canMoveOnOpponent(matrixCopy, selectedPawn.getPosition(), new Position(i, j))))
                            availableCells.remove(new Position(i, j));
                    }else{
                        if (matrixCopy[i][j].getPawn() != null && (!canMoveOnOpponent(matrixCopy, selectedPawn.getPosition(), new Position(i, j)))) {
                            availableCells.remove(new Position(i, j));
                        }
                    }
                }
            }
        }
    }
    /**
     * Removes from availableCells the position where selectedPawn can't move because it is its previous position and denyMoveBack=true
     * @param availableCells is the current list of cells where the selectedPawn can move
     */
    private void checkDenyMoveBack(ArrayList<Position> availableCells) {
        if (denyMoveBack) availableCells.remove(selectedPawn.getPreviousPosition());
    }
    /**
     * This function returns true if the selected position is on the perimenter
     * @return the result of the query
     */
    private Boolean isOnPerimeter() {
        Position selectedPawnPosition = new Position(selectedPawn.getPosition().getX(), selectedPawn.getPosition().getY());
        return selectedPawnPosition.getX() == 0 || selectedPawnPosition.getX() == 4 || selectedPawnPosition.getY() == 0 || selectedPawnPosition.getY() == 4;
    }





    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveAction that = (MoveAction) o;
        return Objects.equals(moveUpEnable, that.moveUpEnable) &&
                Objects.equals(swapEnable, that.swapEnable) &&
                Objects.equals(moveOnOpponentEnable, that.moveOnOpponentEnable) &&
                Objects.equals(pushEnable, that.pushEnable) &&
                Objects.equals(denyMoveUpEnable, that.denyMoveUpEnable) &&
                Objects.equals(winDownEnable, that.winDownEnable) &&
                Objects.equals(addMoveIfOn, that.addMoveIfOn) &&
                Objects.equals(denyMoveBack, that.denyMoveBack) &&
                Objects.equals(noWinIfOnPerimeter, that.noWinIfOnPerimeter);
    }
    @Override public int hashCode() {
        return Objects.hash(moveUpEnable, swapEnable, moveOnOpponentEnable, pushEnable, denyMoveUpEnable, winDownEnable, addMoveIfOn, denyMoveBack, noWinIfOnPerimeter);
    }
}

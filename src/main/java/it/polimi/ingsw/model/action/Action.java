package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Observer;
import it.polimi.ingsw.model.Pawn;
import it.polimi.ingsw.model.Position;

import java.util.ArrayList;
import java.util.List;


//TODO: capire se e come gestire le NullPointerException nei costruttori
//TODO: implementare le funzioni mancanti
public abstract class Action {
    protected Boolean isOptional;
    protected ArrayList<Position> notAvailableCell;
    protected Pawn selectedPawn;
    protected Pawn notSelectedPawn;
    protected Position chosenPosition;
    protected final ActionType actionType;
    protected List<Observer> observers = new ArrayList<>();

    /**
     This constructor of Action sets isOptional and creates an internal copy of notAvailableCell before setting it to the private variable
     */
    Action(Boolean isOptional, ArrayList<Position> notAvailableCell,ActionType actionType) {
        this.actionType=actionType;
        this.isOptional = isOptional;
        if (notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
    }

    /**
     * This function is the copy constructor for the class Action
     * By using this method, there is no need to implement Clonable
     * @param toBeCopied this is the original Action to be copied
     */
    Action(Action toBeCopied){
        this.isOptional = toBeCopied.isOptional;
        this.selectedPawn = toBeCopied.selectedPawn;
        this.notSelectedPawn = toBeCopied.notSelectedPawn;
        this.chosenPosition = toBeCopied.chosenPosition;
        this.observers=toBeCopied.observers;
        this.actionType=toBeCopied.actionType;
        if (toBeCopied.notAvailableCell != null) {
            this.notAvailableCell = new ArrayList<>(toBeCopied.notAvailableCell);
        } else {
            this.notAvailableCell = null;
        }
    }
    /**
     * Abstract method which returns a duplicate of this. Implemented in the concrete classes.
     * @return Action
     */
    public abstract Action duplicate();


    public void addObserver(Observer observer){
        this.observers.add(observer);
    }
    public void removeObserver(Observer observer){
        this.observers.remove(observer);
    }

    /**
     * Notify GameLogic only if this is an MoveAction, for ConstructAction you have to notify the observer once
     * chosenBlockType is set!
     * @param chosenPosition the position where to MOVE/CONSTRUCT
     */
    public void setChosenPosition(Position chosenPosition) {
        this.chosenPosition = chosenPosition;
        //TODO: call update function only inside of the sub-classes (remove setChosenPos from here)
        if(this.actionType==ActionType.MOVE){
            for(Observer observer : this.observers){
                observer.update(this);
            }
        }
    }

    public void setSelectedPawn(Pawn selectedPawn) {
        this.selectedPawn = selectedPawn;
    }

    public void setNotSelectedPawn(Pawn notSelectedPawn) {
        this.notSelectedPawn = notSelectedPawn;
    }

    public Boolean getIsOptional() {
        return isOptional;
    }

    public ArrayList<Position> getNotAvailableCell() {
        return notAvailableCell;
    }

    public Pawn getSelectedPawn() {
        return selectedPawn;
    }

    public Pawn getNotSelectedPawn() {
        return notSelectedPawn;
    }

    public Position getChosenPosition() {
        return chosenPosition;
    }

    public ActionType getActionType() {
        return actionType;
    }

}

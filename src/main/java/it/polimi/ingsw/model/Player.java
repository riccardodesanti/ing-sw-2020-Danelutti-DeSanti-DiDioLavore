package it.polimi.ingsw.model;


import java.util.ArrayList;

/**
 * This class represents every player in the game
 */
class Player {
    final String name;
    ArrayList<Pawn> pawnList;
    Card currentCard;
    Boolean isLoser;
    Boolean isWinner;
    Position selectedPawnPosition;
    Position unselectedPawnPosition;

                                            //CONSTRUCTORS

    /**
     * Constructor of this class
     * @param name defines the name of the player
     */
    Player(String name) {
        this.name = name;
        this.isLoser=false;
        this.isWinner=false;
        pawnList = new ArrayList<>();
    }
    /**
     * This is the copy constructor for the player class
     * @param toBeCopied the player to be copied
     */
    Player(Player toBeCopied){
        this.unselectedPawnPosition=toBeCopied.unselectedPawnPosition;
        this.selectedPawnPosition=toBeCopied.selectedPawnPosition;
        this.name=toBeCopied.name;
        this.pawnList=new ArrayList<>();
        for(Pawn p: toBeCopied.pawnList){
            this.pawnList.add(new Pawn(p));
        }
        this.isWinner=toBeCopied.isWinner;
        this.isLoser=toBeCopied.isLoser;
        this.currentCard=new Card(toBeCopied.currentCard);
    }




                                            //SPECIFIC FUNCTIONS

    /**
     * addPawn method to add the provided Pawn to the pawnList
     * @param pawn is the pawn to add
     */
    void addPawn(Pawn pawn) {
        this.pawnList.add(pawn);
    }
    /**
     * removePawn method to remove the provided Pawn from the pawnList
     * @param pawn is the pawn to remove
     */
    void removePawn(Pawn pawn) {
        if(pawn==null){
            return;
        }
        this.pawnList.remove(pawn);
    }
    /**
     * This function will remove all the pawns for the selected user
     */
    void removeAllPawns(){
        this.pawnList=new ArrayList<>();
    }




                                        //SETTERS

    void setUnselectedPawnPosition(Pawn p){
        if(p==null) {
            unselectedPawnPosition = null;
        }
        else {
            unselectedPawnPosition = new Position(p.getPosition().getX(), p.getPosition().getY());
        }
    }
    void setSelectedPawnPosition(Pawn p){
        if(p==null){
            selectedPawnPosition=null;
        }
        else {
            selectedPawnPosition = new Position(p.getPosition().getX(), p.getPosition().getY());
        }
    }
    void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }
    void setLoser() {
        isLoser = true;
    }
    void setWinner() {
        isWinner = true;
    }




                                        //GETTERS
    Pawn getSelectedPawn(){
        for(Pawn p : pawnList){
            if(p.getPosition().equals(selectedPawnPosition)){
                return p;
            }
        }
        return null;
    }
    Pawn getUnselectedPawn(){
        for(Pawn p : pawnList){
            if(p.getPosition().equals(unselectedPawnPosition)){
                return p;
            }
        }
        return null;
    }
    Boolean getLoser() {
        return isLoser;
    }
    Boolean getWinner() {
        return isWinner;
    }
    String getName() {
        return name;
    }
    ArrayList<Pawn> getPawnList() {
        return pawnList;
    }
    Card getCurrentCard() {
        return currentCard;
    }

}

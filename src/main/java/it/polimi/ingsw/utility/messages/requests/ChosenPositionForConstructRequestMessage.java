package it.polimi.ingsw.utility.messages.requests;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class ChosenPositionForConstructRequestMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -4316648528033190504L;

    public ArrayList<Position> getAvailablePositions() {
        return availablePositions;
    }

    ArrayList<Position> availablePositions=new ArrayList<>();

    public ChosenPositionForConstructRequestMessage(ArrayList<String> recipients, ArrayList<Position> availablePositions) {
        super(recipients);
        this.availablePositions.addAll(availablePositions);
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}

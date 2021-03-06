package it.polimi.ingsw.utility.messages.updates;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.RequestAndUpdateObservable;
import it.polimi.ingsw.utility.messages.RequestAndUpdateMessage;

import java.io.Serializable;
import java.util.ArrayList;

public class PawnPositionUpdateMessage extends RequestAndUpdateMessage implements Serializable {
    private static final long serialVersionUID = -5206036861451704094L;

    int workerId;
    Position workerPos;

    public PawnPositionUpdateMessage(ArrayList<String> recipients, int workerId, Position workerPos) {
        super(recipients);
        this.workerId = workerId;
        this.workerPos = workerPos;
    }

    public int getWorkerId() {
        return workerId;
    }

    public Position getWorkerPos() {
        return workerPos;
    }

    public void accept(RequestAndUpdateObservable visitor) {
        visitor.notifyListeners(this);
    }
}

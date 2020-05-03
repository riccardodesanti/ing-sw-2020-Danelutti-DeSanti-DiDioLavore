package it.polimi.ingsw.utility.messages.sets;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.utility.messages.Message;

import java.util.ArrayList;

public class InitialPawnPositionSetMessage extends Message {
    int workerId1,workerId2;
    Position workerPos1, workerPos2;

    public InitialPawnPositionSetMessage(ArrayList<String> recipients, int workerId1, int workerId2, Position workerPos1, Position workerPos2) {
        super(recipients);
        this.workerId1 = workerId1;
        this.workerId2 = workerId2;
        this.workerPos1 = workerPos1;
        this.workerPos2 = workerPos2;
    }
}
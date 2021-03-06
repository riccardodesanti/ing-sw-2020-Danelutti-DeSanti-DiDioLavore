package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.RequestAndUpdateObservable;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class RequestAndUpdateMessage extends Message implements Serializable {
    private static final long serialVersionUID = 4130022452646135290L;


    ArrayList<String> recipients = new ArrayList<>();

    public ArrayList<String> getRecipients() {
        return recipients;
    }
    public RequestAndUpdateMessage(ArrayList<String> recipients) {
        this.recipients.addAll(recipients);
    }

    public abstract void accept(RequestAndUpdateObservable visitor);
}

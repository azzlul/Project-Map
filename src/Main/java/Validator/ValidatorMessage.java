package Validator;

import Domain.Message;
import Exceptions.ValidatorException;

import java.util.Objects;

public class ValidatorMessage extends ValidatorInt<Message>{
    @Override
    public void validate(Message message) {
        String errors = "";
        if(message.getFromID() <= 0) errors += "From user ID is invalid!\n";
        if(message.getToID() <= 0) errors += "To user ID is invalid!\n";
        if(Objects.equals(message.getMessage(), "")) errors += "Message is empty!\n";
        if(message.getMessage().length() > 200) errors += "Message is too long!\n";
        if(message.getReplyID() < -1)errors += "Reply ID is invalid!\n";
        if(!errors.isEmpty()) throw new ValidatorException(errors);
    }
}

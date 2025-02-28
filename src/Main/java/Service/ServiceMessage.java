package Service;

import Domain.Friendship;
import Domain.Message;
import Exceptions.ServiceException;
import Repository.Database.FriendshipDbRepo;
import Repository.Database.MessageDbRepo;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class ServiceMessage {
    private final MessageDbRepo repo;
    public ServiceMessage(MessageDbRepo repo) {
        this.repo = repo;
    }

    public void add(int toId, int fromId, String message, LocalDateTime date, int replyId) {
        Message msg = new Message(toId, fromId, message, date, replyId);
        var result = repo.save(msg);
        if(result.isPresent()) throw new ServiceException("Message could not be added");
    }

    public void update(int id, int fromId, int toId, String message, LocalDateTime date, int replyId) {
        Message msg = new Message(id, fromId, toId, message, date, replyId);
        var result = repo.update(msg);
        if(result.isPresent()) throw new ServiceException("Message could not be updated");
    }

    public void remove(int id) {
        var result = repo.delete(id);
        if(result.isEmpty()) throw new ServiceException("Message could not be removed");
    }

    public Message find(int id) {
        var result = repo.findOne(id);
        if(result.isEmpty()) throw new ServiceException("Message could be not found");
        return result.get();
    }
    public Iterable<Message> findMessagesFromUsers(int userid1, int userid2) {
        return repo.findAllMessagesFromUsers(userid1, userid2);
    }

    public Message findLastMessageFromUsers(int userid1, int userid2) {
        return repo.findLastMessageFromUsers(userid1, userid2);
    }
    public Iterable<Message> findAll() {
        return repo.findAll();
    }

    public int size(){
        return repo.size();
    }
}

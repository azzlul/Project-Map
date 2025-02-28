package Repository.Database;

import Domain.Friendship;
import Domain.Message;
import Validator.ValidatorMessage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class MessageDbRepo extends AbstractDbRepo<Integer, Message>{

    public MessageDbRepo(Connection connection) {
        validator = new ValidatorMessage();
        this.connection = connection;
    }
    @Override
    protected Message readEntity(ResultSet entity) throws SQLException {
        return new Message(entity.getInt("messageid"), entity.getInt("fromid"), entity.getInt("toid"),
                entity.getString("message"), entity.getTimestamp("date").toLocalDateTime(), entity.getInt("replyid"));
    }

    @Override
    protected void insertDb(Message entity) throws SQLException {
        var st = connection.prepareStatement("insert into messages(fromid, toid, message, date, replyid) values(?,?,?,?,?)");
        st.setInt(1, entity.getFromID());
        st.setInt(2, entity.getToID());
        st.setString(3, entity.getMessage());
        st.setTimestamp(4, Timestamp.valueOf(entity.getDateTime()));
        st.setInt(5, entity.getReplyID());
        st.executeUpdate();
    }

    @Override
    protected void updateDb(Message entity) throws SQLException {
        var st = connection.prepareStatement("update messages set fromid=?, toid=?, message=?, date=?, replyid=? where messageid=?");
        st.setInt(1, entity.getFromID());
        st.setInt(2, entity.getToID());
        st.setString(3, entity.getMessage());
        st.setTimestamp(4, Timestamp.valueOf(entity.getDateTime()));
        st.setInt(5, entity.getReplyID());
        st.setInt(6, entity.getId());
        st.executeUpdate();
    }

    @Override
    protected void deleteDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("delete from messages where messageid=?");
        st.setInt(1, integer);
        st.executeUpdate();
    }

    @Override
    protected ResultSet findDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("select * from messages where messageid=?");
        st.setInt(1, integer);
        return st.executeQuery();
    }

    @Override
    protected ResultSet findAllDb() throws SQLException {
        var st = connection.prepareStatement("select * from messages");
        return st.executeQuery();
    }

    @Override
    protected ResultSet sizeDb() throws SQLException {
        var st = connection.prepareStatement("select count(*) from messages");
        return st.executeQuery();
    }

    protected ResultSet findAllMessagesFromUsersDb(int userid1, int userid2) throws SQLException {
        var st = connection.prepareStatement("select * from messages where (toid = ? and fromid = ?) or (fromid = ? and toid = ?) order by date");
        st.setInt(1, userid1);
        st.setInt(2, userid2);
        st.setInt(3, userid1);
        st.setInt(4, userid2);
        return st.executeQuery();
    }

    public Iterable<Message> findAllMessagesFromUsers(int userid1, int userid2){
        try{
            var rez = findAllMessagesFromUsersDb(userid1, userid2);
            var set = new ArrayList<Message>();
            while(rez.next()) {
                set.add(readEntity(rez));
            }
            return set;
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    protected ResultSet findLastMessageFromUsersDb(int userid1, int userid2) throws SQLException {
        var st = connection.prepareStatement("select * from messages where ((toid = ? and fromid = ?)or(fromid = ? and toid = ?))" +
                " and date = (select max(date) from messages)");
        st.setInt(1, userid1);
        st.setInt(2, userid2);
        st.setInt(3, userid1);
        st.setInt(4, userid2);
        return st.executeQuery();
    }
    public Message findLastMessageFromUsers(int userid1, int userid2){
        try {
            var rez = findLastMessageFromUsersDb(userid1, userid2);
            rez.next();
            return readEntity(rez);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

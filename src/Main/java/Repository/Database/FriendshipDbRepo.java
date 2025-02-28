package Repository.Database;

import Domain.Friendship;
import Exceptions.RepositoryException;
import Validator.*;
import utils.Page;
import utils.Pageable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;

public class FriendshipDbRepo extends AbstractDbRepo<Integer, Friendship> {
    public FriendshipDbRepo(Connection connection) {
        validator = new ValidatorFriendship();
        this.connection = connection;
    }
    @Override
    protected Friendship readEntity(ResultSet entity) throws SQLException {
        return new Friendship(entity.getInt("friendshipid"), entity.getInt("userid1"), entity.getInt("userid2"),
                entity.getTimestamp("friendsfrom").toLocalDateTime(), entity.getInt("firstmessageid"), entity.getBoolean("accepted"));
    }

    @Override
    protected void insertDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("insert into friendships (userid1, userid2, friendsfrom, accepted, firstmessageid) values(?,?,?,?,?)");
        st.setInt(1, entity.getFirstUserID());
        st.setInt(2, entity.getSecondUserID());
        st.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
        st.setBoolean(4,entity.isAccepted());
        st.setInt(5, entity.getFirstMessageID());
        st.executeUpdate();
    }

    @Override
    protected void updateDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("update friendships set userid1 = ?, userid2 = ?, friendsfrom = ?," +
                                                 "accepted = ?, firstmessageid = ? where friendshipid = ?");
        st.setInt(1, entity.getFirstUserID());
        st.setInt(2, entity.getSecondUserID());
        st.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
        st.setBoolean(4,entity.isAccepted());
        st.setInt(5, entity.getFirstMessageID());
        st.setInt(6, entity.getId());
        st.executeUpdate();
    }

    @Override
    protected void deleteDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("delete from friendships where friendshipid = ?");
        st.setInt(1, integer);
        st.executeUpdate();
    }

    @Override
    protected ResultSet findDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("select * from friendships where friendshipid = ?");
        st.setInt(1, integer);
        return st.executeQuery();
    }

    @Override
    protected ResultSet findAllDb() throws SQLException {
        var st = connection.prepareStatement("select * from friendships");
        return st.executeQuery();
    }

    @Override
    protected ResultSet sizeDb() throws SQLException {
        var st = connection.prepareStatement("select count(*) from friendships");
        return st.executeQuery();
    }

    private ResultSet findFriendsOfUserDb(Integer userid) throws SQLException {
        var st = connection.prepareStatement("select * from friendships where (userid1 = ? or userid2 = ?) and accepted = true");
        st.setInt(1, userid);
        st.setInt(2, userid);
        return st.executeQuery();
    }

    private ResultSet findNumberOfFriendsDb(Integer userid) throws SQLException {
        var st = connection.prepareStatement("select count(*) from friendships where (userid1 = ? or userid2 = ?) and accepted = true");
        st.setInt(1, userid);
        st.setInt(2, userid);
        return st.executeQuery();
    }

    private ResultSet findFriendsOfUserDbPaged(Integer userid, Pageable pageable) throws SQLException {
        var st = connection.prepareStatement("select * from friendships where (userid1 = ? or userid2 = ?) and accepted = true limit ? offset ?");
        st.setInt(1, userid);
        st.setInt(2, userid);
        st.setInt(3, pageable.getPageSize());
        st.setInt(4, pageable.getPageSize() * pageable.getPageNumber());
        return st.executeQuery();
    }

    private ResultSet findFriendRequestsToUserDb(Integer userid) throws SQLException {
        var st = connection.prepareStatement("select * from friendships where userid2 = ? and accepted = false");
        st.setInt(1, userid);
        return st.executeQuery();
    }

    public Iterable<Friendship> findFriendsOfUser(Integer userid){
        try {
            var result = findFriendsOfUserDb(userid);
            var set = new HashSet<Friendship>();
            while(result.next()) {
                set.add(readEntity(result));
            }
            return set;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int findNumberOfFriends(int userID){
        try {
            var result = findNumberOfFriendsDb(userID);
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Page<Friendship> findFriendsOfUserPaged(Integer userid, Pageable pageable){
        try {
            var result = findFriendsOfUserDbPaged(userid, pageable);
            var set = new HashSet<Friendship>();
            while(result.next()) {
                set.add(readEntity(result));
            }
            return new Page<>(set,findNumberOfFriends(userid));
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Iterable<Friendship> findFriendRequestsToUser(Integer userid){
        try {
            var result = findFriendRequestsToUserDb(userid);
            var set = new HashSet<Friendship>();
            while(result.next()) {
                set.add(readEntity(result));
            }
            return set;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

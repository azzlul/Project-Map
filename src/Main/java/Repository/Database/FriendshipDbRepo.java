package Repository.Database;

import Domain.Friendship;
import Exceptions.RepositoryException;
import Validator.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FriendshipDbRepo extends AbstractDbRepo<Integer, Friendship> {
    public FriendshipDbRepo(Connection connection) {
        validator = new ValidatorFriendship();
        this.connection = connection;
    }
    @Override
    protected Friendship readEntity(ResultSet entity) throws SQLException {
        return new Friendship(entity.getInt("friendshipid"), entity.getInt("userid1"), entity.getInt("userid2"),
                entity.getTimestamp("friendsfrom").toLocalDateTime(), entity.getBoolean("accepted"));
    }

    @Override
    protected void insertDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("insert into friendships (userid1, userid2, friendsfrom, accepted) values(?,?,?,?)");
        st.setInt(1, entity.getFirstUserID());
        st.setInt(2, entity.getSecondUserID());
        st.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
        st.setBoolean(4,entity.isAccepted());
        st.executeUpdate();
    }

    @Override
    protected void updateDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("update friendships set userid1 = ?, userid2 = ?, friendsfrom = ?," +
                                                 "accepted = ? where friendshipid = ?");
        st.setInt(1, entity.getFirstUserID());
        st.setInt(2, entity.getSecondUserID());
        st.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
        st.setBoolean(4,entity.isAccepted());
        st.setInt(5, entity.getId());
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
}

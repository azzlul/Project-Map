package Repository.Database;

import Domain.Friendship;
import Exceptions.RepositoryException;
import Validator.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipDbRepo extends AbstractDbRepo<Integer, Friendship> {
    public FriendshipDbRepo(Connection connection) {
        validator = new ValidatorFriendship();
        this.connection = connection;
    }
    @Override
    protected Friendship readEntity(ResultSet entity) throws SQLException {
        return new Friendship(entity.getInt("friendshipid"), entity.getInt("userid1"), entity.getInt("userid2"));
    }

    @Override
    protected void insertDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("insert into friendships values(?,?,?)");
        st.setInt(1, entity.getId());
        st.setInt(2, entity.getFirstUserID());
        st.setInt(3, entity.getSecondUserID());
        st.executeUpdate();
    }

    @Override
    protected void updateDb(Friendship entity) throws SQLException {
        var st = connection.prepareStatement("update friendships set firstUserID = ?, secondUserID = ? where friendshipid = ?");
        st.setInt(1, entity.getFirstUserID());
        st.setInt(2, entity.getSecondUserID());
        st.setInt(3, entity.getId());
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

    @Override
    protected Integer generateID() {
        try{
            var st = connection.prepareStatement("select max(friendshipid) from friendships");
            var result = st.executeQuery();
            if(!result.next()) return 1;
            return result.getInt(1) + 1;
        }
        catch (SQLException e){
            throw new RepositoryException(e.getMessage());
        }
    }
}

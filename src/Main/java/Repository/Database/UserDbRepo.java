package Repository.Database;

import Domain.User;
import Exceptions.RepositoryException;
import Validator.ValidatorUser;
import utils.Page;
import utils.Pageable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserDbRepo extends AbstractDbRepo<Integer, User> {
    public UserDbRepo(Connection connection) {
        validator = new ValidatorUser();
        this.connection = connection;
    }

    @Override
    protected User readEntity(ResultSet entity) throws SQLException {
        return new User(entity.getInt("userid"), entity.getString("username"));
    }

    @Override
    protected void insertDb(User entity) throws SQLException {
        var st = connection.prepareStatement("insert into users(username) values(?)");
        st.setString(1, entity.getName());
        st.executeUpdate();
    }

    @Override
    protected void updateDb(User entity) throws SQLException {
        var st = connection.prepareStatement("update users set username = ? where userid = ?");
        st.setString(1, entity.getName());
        st.setInt(2, entity.getId());
        st.executeUpdate();
    }

    @Override
    protected void deleteDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("delete from users where userid = ?");
        st.setInt(1, integer);
        st.executeUpdate();
    }

    @Override
    protected ResultSet findDb(Integer integer) throws SQLException {
        var st = connection.prepareStatement("select * from users where userid = ?");
        st.setInt(1, integer);
        return st.executeQuery();
    }

    @Override
    protected ResultSet findAllDb() throws SQLException {
        var st = connection.prepareStatement("select * from users");
        return st.executeQuery();
    }

    @Override
    protected ResultSet sizeDb() throws SQLException {
        var st = connection.prepareStatement("select count(*) from users");
        return st.executeQuery();
    }

    protected ResultSet findAllOnPageDb(Pageable pageable) throws SQLException {
        var st = connection.prepareStatement("select * from users limit ? offset ?");
        st.setInt(1, pageable.getPageSize());
        st.setInt(2, pageable.getPageSize() * pageable.getPageNumber());
        return st.executeQuery();
    }

    public Page<User> findAllOnPage(Pageable pageable) {
        try{
            var result = findAllOnPageDb(pageable);
            var set = new HashSet<User>();
            while(result.next()) {
                set.add(readEntity(result));
            }
            return new Page<>(set, size());
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

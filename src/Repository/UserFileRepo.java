package Repository;
import Domain.User;
import Validator.ValidatorUser;

/**
 * File repository for users.
 */
public class UserFileRepo extends AbstractFileRepoIntID<User>{
    /**
     * Constructor for class.
     * @param file_path path to file
     */
    public UserFileRepo(String file_path) {
        super(file_path);
        validator = new ValidatorUser();
    }

    @Override
    public User readEntity(String line) {
        String[] split = line.split(";");
        User u = new User(split[1]);
        u.setId(Integer.parseInt(split[0]));
        return u;
    }

    @Override
    public String writeEntity(User entity) {
        return entity.getId() + ";" + entity.getName();
    }
}

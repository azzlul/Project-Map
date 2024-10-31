package Repository;
import Domain.User;
import Validator.ValidatorUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
        ArrayList<Integer> friendsID = new ArrayList<>();
        split[2] = split[2].replace("[", "");
        split[2] = split[2].replace("]", "");
        if(!split[2].isEmpty())  Arrays.stream(split[2].split(",")).forEach(ch -> friendsID.add(Integer.parseInt(ch)));
        User u = new User(split[1], friendsID);
        u.setId(Integer.parseInt(split[0]));
        return u;
    }

    @Override
    public String writeEntity(User entity) {
        return entity.getId() + ";" + entity.getName() + ";" + entity.getFriendsID();
    }
}

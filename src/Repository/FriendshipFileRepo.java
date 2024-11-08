package Repository;
import Domain.Friendship;
import Validator.ValidatorFriendship;

/**
 * File repository for friendships.
 */
public class FriendshipFileRepo extends AbstractFileRepoIntID<Friendship>{
    /**
     * Constructor for class.
     * @param file_path path to file
     */
    public FriendshipFileRepo(String file_path) {
        super(file_path);
        validator = new ValidatorFriendship();
    }

    @Override
    protected Friendship readEntity(String line) {
        String[] split = line.split(";");
        Friendship f = new Friendship(Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        f.setId(Integer.parseInt(split[0]));
        return f;
    }

    @Override
    protected String writeEntity(Friendship entity) {
        return entity.getId() + ";" + entity.getFirstUserID() + ";" + entity.getSecondUserID();
    }
}

package org.vaadin.example.rsa.user;


import org.vaadin.example.rsa.RideSharingAppException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A collection of users. Contains methods for registration, authentication and retrieving users and their names.
 * Nicks acts as keys and cannot be changed.
 * They must be a single word (no white characters) of letters, digits and underscores, starting with a letter
 * Users data is serialized for persistence.
 * @version 1.0
 * @see <a href="https://www.dcc.fc.up.pt/~zp/aulas/2425/asw/api/rsa/user/Users.html">Official API Documentation</a>
 */

//TODO - Make Serialization work. load()/save()
public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID =  1L;
    /// Maps User nicks to User instances
    private final Map<String, User> users = new HashMap<>();
    private static File usersFile;
    private static Users instance;

    /**
     * Private constructor - singleton
     */
    private Users() {}

    /**
     * Name of file containing users' data
     * @return file containing serialization
     */
    public static File getUsersFile() {
        return usersFile;
    }

    /**
     * Change pathname of file containing users' data
     * @param usersFile contain serialization
     */
    public static void setUsersFile(File usersFile) {
        Users.usersFile = usersFile;
    }

    /**
     * Returns the single instance of this class as proposed in the <b>singleton</b> design pattern.
     * If a backup of this class is available then the users instance is recreated from that data
     * @return instance of this class
     * @throws RideSharingAppException if I/O error occurs reading serialization
     */
    public static Users getInstance() throws RideSharingAppException {
            if (instance == null) {
                if (usersFile != null && usersFile.exists()) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile))) {
                        instance = (Users) ois.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RideSharingAppException("Failed to load users from file", e);
                    }
                }else {
                    instance = new Users();
                    }
                }

            return instance;
        }

    /// Resets singleton for unit testing purposes.
    public void reset() {
        users.clear();
        instance = null;
        if (usersFile != null && usersFile.exists()) {
            usersFile.delete();
        }
    }

    /// Confirms that a given string only has letters and numbers
    /// @param nick user nick to be validated
    private boolean isValidNick(String nick) {
        if (nick == null) return false;
        return nick.matches("[a-zA-Z0-9]+");
    }

    /**
     * Save users data to file.
     * @throws RideSharingAppException if there is an I/O error
     */
    private void saveToFile() throws RideSharingAppException {
        if (usersFile == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(usersFile))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new RideSharingAppException("Failed to save users data", e);
        }
    }

    /**
     * Register a player with given nick and name.
     * Changes are immediately serialized.
     * Nicks can have letters (upper and lowercase) and digits but not other characters.
     * @param nick - of user
     * @param name - of user
     * @return user with given nick and name, or null if nick already exists or is invalid.
     * @throws RideSharingAppException - on I/O error in serialization
     */
    public User register(String nick , String name) throws RideSharingAppException {
        //Nick is invalid or nick is taken
        if(!isValidNick(nick) || users.containsKey(nick)) return null;
        //Name must not have excessive spacing
        if(name == null || name.trim().isEmpty()) return null;

        var user= new User(nick, name);
        users.put(nick,user);

        saveToFile();
        return user;
    }

    /**
     * Get the user with given nick
     * @param nick of player
     * @return User instance mapped to given nick
     */
    public User getUser(String nick) {
        return users.get(nick);
    }

    /**
     * Get existing user with nick, or register a new one if the user does not exist.
     * Used in unit testing.
     * @param nick - of user
     * @param name - of user
     * @return user with given nick (and name)
     * @throws RideSharingAppException - on serialization error.
     */
    public User getOrCreateUser(String nick, String name) throws RideSharingAppException {
        User user = getUser(nick);
        return (user != null) ? user : register(nick, name);
    }

    /**
     * Authenticate user with user key
     * @param nick of user
     * @param key of user
     * @return {@code true} if {@code nick} exists and {@code key} is valid; {@code false} otherwise
     */
    public boolean authenticate(String nick, String key) {
        User user = getUser(nick);
        return user!=null && user.authenticate(key);
    }
}

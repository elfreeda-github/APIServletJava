package com.he.api;

import java.util.List;


/**
 *
 * @author divyansh
 */
public interface UserDAO {
    public List<User> getAllUsers();
    public List<User> searchByCity(String city);
    public List<User> searchByFirstName(String firstName);
    public List<User> searchByLastName(String lastName);
    public User searchById(String id);
    public void insertUser(User user);
    public void updateUser(User user);
    public void deleteUser(String id);
}

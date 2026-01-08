package LLD.splitwise.repository;

import LLD.splitwise.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryUserRepository implements UserRepository {

    private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public void addUser(User user) {
        if (user == null) return;
        users.put(user.getUserId(), user);
    }

    @Override
    public User getUser(String userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}


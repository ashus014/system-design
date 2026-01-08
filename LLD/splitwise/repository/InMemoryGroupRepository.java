package LLD.splitwise.repository;

import LLD.splitwise.group.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryGroupRepository implements GroupRepository {

    private final ConcurrentMap<String, Group> groups = new ConcurrentHashMap<>();

    @Override
    public void addGroup(Group group) {
        if (group == null) return;
        groups.put(group.getGroupId(), group);
    }

    @Override
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }

    @Override
    public List<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }
}


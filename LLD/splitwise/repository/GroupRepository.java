package LLD.splitwise.repository;

import LLD.splitwise.group.Group;
import java.util.List;

public interface GroupRepository {
    void addGroup(Group group);
    Group getGroup(String groupId);
    List<Group> getAllGroups();
}


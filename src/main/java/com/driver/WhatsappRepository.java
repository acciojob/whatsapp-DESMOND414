package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
//    private HashMap<Group, List<User>> groupUserMap;
//    private HashMap<Group, List<Message>> groupMessageMap;

    public WhatsappRepository(Map<String, User> usersMap, Map<String, Group> groupsMap, Map<Group, List<User>> groupUserMap, Map<Group, List<Message>> groupMessageMap, Map<Message, User> senderMap, Map<Group, User> adminMap, Set<String> userMobileSet, int customGroupCount, int messageId) {
        this.usersMap = usersMap;
        this.groupsMap = groupsMap;
        this.groupUserMap = groupUserMap;
        this.groupMessageMap = groupMessageMap;
        this.senderMap = senderMap;
        this.adminMap = adminMap;
        this.userMobileSet = userMobileSet;
        this.customGroupCount = customGroupCount;
        this.messageId = messageId;
    }
//    private HashMap<Message, User> senderMap;

    public WhatsappRepository() {
    }
//    private HashMap<Group, User> adminMap;
//    private HashSet<String> userMobile;
//    private int customGroupCount;
//    private int messageId;

    private Map<String, User> usersMap;
    private Map<String, Group> groupsMap;
    private Map<Group, List<User>> groupUserMap;
    private Map<Group, List<Message>> groupMessageMap;
    private Map<Message, User> senderMap;
    private Map<Group, User> adminMap;
    private Set<String> userMobileSet;
    private int customGroupCount;
    private int messageId;

//    private Map<String, User> usersMap;

    public Map<String, User> getUsersMap() {
        return usersMap;
    }

    public void setUsersMap(Map<String, User> usersMap) {
        this.usersMap = usersMap;
    }

    public Map<String, Group> getGroupsMap() {
        return groupsMap;
    }

    public void setGroupsMap(Map<String, Group> groupsMap) {
        this.groupsMap = groupsMap;
    }

    public Map<Group, List<User>> getGroupUserMap() {
        return groupUserMap;
    }

    public void setGroupUserMap(Map<Group, List<User>> groupUserMap) {
        this.groupUserMap = groupUserMap;
    }

    public Map<Group, List<Message>> getGroupMessageMap() {
        return groupMessageMap;
    }

    public void setGroupMessageMap(Map<Group, List<Message>> groupMessageMap) {
        this.groupMessageMap = groupMessageMap;
    }

    public Map<Message, User> getSenderMap() {
        return senderMap;
    }

    public void setSenderMap(Map<Message, User> senderMap) {
        this.senderMap = senderMap;
    }

    public Map<Group, User> getAdminMap() {
        return adminMap;
    }

    public void setAdminMap(Map<Group, User> adminMap) {
        this.adminMap = adminMap;
    }

    public Set<String> getUserMobileSet() {
        return userMobileSet;
    }

    public void setUserMobileSet(Set<String> userMobileSet) {
        this.userMobileSet = userMobileSet;
    }

    public int getCustomGroupCount() {
        return customGroupCount;
    }

    public void setCustomGroupCount(int customGroupCount) {
        this.customGroupCount = customGroupCount;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
//    private Map<String, Group> groupsMap;
//    private List<Message> messagesList;

//    public WhatsappRepository() {
//        this.groupMessageMap = new HashMap<Group, List<Message>>();
//        this.groupUserMap = new HashMap<Group, List<User>>();
//        this.senderMap = new HashMap<Message, User>();
//        this.adminMap = new HashMap<Group, User>();
//        this.userMobile = new HashSet<>();
//        this.customGroupCount = 0;
//        this.messageId = 0;
//    }

    public String createUser(String name, String mobile) throws Exception {
        if (userMobileSet.contains(mobile)) {
            throw new Exception("User already exists");
        }
        User user = new User(name, mobile);
        usersMap.put(mobile, user);
        userMobileSet.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        User admin = users.get(0);
        String groupName;
        if (users.size() == 2) {
            groupName = users.get(1).getName();
        } else {
            customGroupCount++;
            groupName = "Group " + customGroupCount;
        }
        Group group = new Group(groupName, users.size());
        groupsMap.put(groupName, group);
        groupUserMap.put(group, users);
        adminMap.put(group, admin);
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Message message = new Message(messageId, content);
        message.setTimestamp(new Date());
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if (!groupsMap.containsKey(group.getName())) {
            throw new Exception("Group does not exist");
        }

        if (!groupUserMap.get(group).contains(sender)) {
            throw new Exception("You are not allowed to send a message");
        }

        List<Message> groupMessages = groupMessageMap.getOrDefault(group, new ArrayList<>());
        groupMessages.add(message);
        groupMessageMap.put(group, groupMessages);

        senderMap.put(message, sender);

        return groupMessages.size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if (!groupsMap.containsKey(group.getName())) {
            throw new Exception("Group does not exist");
        }

        User currentAdmin = adminMap.get(group);

        if (!currentAdmin.equals(approver)) {
            throw new Exception("Approver does not have rights");
        }

        if (!groupUserMap.get(group).contains(user)) {
            throw new Exception("User is not a participant");
        }

        adminMap.put(group, user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception {
        Group userGroup = null;
        for (Group group : groupUserMap.keySet()) {
            List<User> userList = groupUserMap.get(group);
            if (userList.contains(user)) {
                userGroup = group;
                break;
            }
        }
        if (userGroup == null) {
            throw new Exception("User not found");
        }

        if (adminMap.get(userGroup).equals(user)) {
            throw new Exception("Cannot remove admin");
        }

        groupUserMap.get(userGroup).remove(user);
        return groupUserMap.get(userGroup).size();
    }

    public List<Message> getGroupMessages(Group group) throws Exception {
        if (!groupsMap.containsKey(group.getName())) {
            throw new Exception("Group does not exist");
        }

        return groupMessageMap.getOrDefault(group, new ArrayList<>());
    }

    public List<User> getGroupParticipants(Group group) throws Exception {
        if (!groupsMap.containsKey(group.getName())) {
            throw new Exception("Group does not exist");
        }

        return groupUserMap.getOrDefault(group, new ArrayList<>());
    }

    public User getMessageSender(Message message) throws Exception {
        if (!senderMap.containsKey(message)) {
            throw new Exception("Message does not exist");
        }

        return senderMap.get(message);
    }

    public List<Group> getUserGroups(User user) {
        List<Group> userGroups = new ArrayList<>();
        for (Group group : groupUserMap.keySet()) {
            List<User> userList = groupUserMap.get(group);
            if (userList.contains(user)) {
                userGroups.add(group);
            }
        }
        return userGroups;
    }

    public User getUserByMobile(String mobile) throws Exception {
        if (!usersMap.containsKey(mobile)) {
            throw new Exception("User does not exist");
        }

        return usersMap.get(mobile);
    }

    public String findMessages(Date startDate, Date endDate, int K) throws Exception {
        List<Message> messagesInRange = new ArrayList<>();

        for (Group group : groupMessageMap.keySet()) {
            List<Message> groupMessages = groupMessageMap.get(group);
            for (Message message : groupMessages) {
                Date timestamp = message.getTimestamp();
                if (timestamp.after(startDate) && timestamp.before(endDate)) {
                    messagesInRange.add(message);
                }
            }
        }

        if (messagesInRange.size() < K) {
            throw new Exception("K is greater than the number of messages");
        }

        messagesInRange.sort(Comparator.comparing(Message::getTimestamp).reversed());
        List<Message> latestMessages = messagesInRange.subList(0, K);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < latestMessages.size(); i++) {
            Message message = latestMessages.get(i);
            result.append("Message ").append(i + 1).append(": ").append(message.getContent()).append("\n");
        }

        return result.toString();
    }

}

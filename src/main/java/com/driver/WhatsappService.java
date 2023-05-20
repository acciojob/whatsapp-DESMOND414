package com.driver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class WhatsappService {

    public WhatsappService(WhatsappRepository whatsappRepository) {
        this.whatsappRepository = whatsappRepository;
    }


    WhatsappRepository whatsappRepository=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        return whatsappRepository.createUser(name,mobile);
    }

    public Group createGroup(List<User> users) {
        return whatsappRepository.createGroup(users);
    }

    public int createMessage(String content) {

        return whatsappRepository.createMessage(content);
    }

    public WhatsappService() {
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        return whatsappRepository.sendMessage(message,sender,group);
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        return whatsappRepository.changeAdmin(approver,user,group);
    }

    public int removeUser(User user) throws Exception {
        return whatsappRepository.removeUser(user);
        }

    public String findMessage(Date start, Date end, int k) throws Exception {
        return whatsappRepository.findMessages(start,end,k);
    }
}

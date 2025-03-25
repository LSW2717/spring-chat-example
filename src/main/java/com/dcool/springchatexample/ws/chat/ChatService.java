package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.api.channels.ChannelRepository;
import com.dcool.springchatexample.api.friends.FriendRepository;
import com.dcool.springchatexample.api.rooms.RoomRepository;
import com.dcool.springchatexample.api.topics.TopicRepository;
import com.dcool.springchatexample.api.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.dcool.springchatexample.domain.Topic;
import org.springframework.stereotype.Component;

@Component
public abstract class ChatService {

    protected Log logger = LogFactory.getLog(getClass());

    protected @Autowired ChannelRepository channelRepository;
    protected @Autowired RoomRepository roomRepository;
    protected @Autowired TopicRepository topicRepository;
    protected @Autowired FriendRepository friendRepository;
    protected @Autowired UserRepository userRepository;

    protected @Autowired ChatSending chatSending;

    protected @Autowired ObjectMapper objectMapper;


    public abstract void process(Topic t);
}

package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Topic;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("ChatService_INVITE")
public class ChatServiceInvite extends ChatService {

    protected Topic.Command command = Topic.Command.INVITE;

    @Override
    public void process(Topic topic)  {

        long timestamp = System.currentTimeMillis();
        String process = "[#"+timestamp+"]: ";
        logger.info(process+topic);

        try {
            String roomId = topic.getRoomId();
            Room room = roomRepository.findById(roomId).get();
            Channel channel = room.getChannel();

            String[] beforeInviteUsers = channel.getInviteUsers();
            logger.info(beforeInviteUsers.length);
            List<String> inviteUsers = new ArrayList<>(Arrays.asList(beforeInviteUsers));


            JsonNode content = objectMapper.convertValue(topic.getContent(), JsonNode.class);
            String inviteUser = content.get("inviteUser").asText();
            //{"inviteUser" : "홍길동"}
            inviteUsers.add(inviteUser);

            Collections.sort(inviteUsers);
            String[] afterInviteUsers = new String[inviteUsers.size()];
            inviteUsers.toArray(afterInviteUsers);
            logger.info(afterInviteUsers.length);

            topic.setTopicId(timestamp);
            topic.setCommand(Topic.Command.INVITE);
            Topic savedTopic = topicRepository.save(topic);
            logger.info(process+"[CREATE]"+savedTopic);

            Room inviteUserRoom = new Room();
            inviteUserRoom.setRoomId(Room.roomId(inviteUser, channel));
            inviteUserRoom.setUserId(inviteUser);
            inviteUserRoom.setChannel(channel);
            inviteUserRoom.setRoomBadge(0);
            inviteUserRoom.setRoomType("chat");
            inviteUserRoom.setRoomNameRef(channel.getChannelName());
            inviteUserRoom.setRoomActive(false);
            inviteUserRoom.setFirstTopic(objectMapper.writeValueAsString(savedTopic));
            inviteUserRoom = roomRepository.save(inviteUserRoom);
            logger.info(process+"[CREATE]"+inviteUserRoom);

            channel.setInviteUsers(afterInviteUsers);
            channel = channelRepository.save(channel);
            logger.info(process+"[UPDATE]"+channel);

            chatSending.broadcastMessage(savedTopic);

        }catch(Exception e){
            logger.info(process, e);
        }
    }
}
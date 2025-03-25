package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Topic;
import com.dcool.springchatexample.domain.converters.StringArrayConverter;
import org.springframework.stereotype.Component;

@Component("ChatService_RECEIVED")
public class ChatServiceReceived extends ChatService {

    protected Topic.Command command = Topic.Command.RECEIVED;

    @Override
    public synchronized void process(Topic topic) {

        long timestamp = System.currentTimeMillis();
        String process = "[#"+timestamp+"]: ";
        logger.info(process+topic);

        try {
            String userId = topic.getUserId();
            Long topicId = (Long) (topic.getContent().get("topicId"));
            Topic savedTopic = topicRepository.findById(topicId).get();

            String[] beforeReadUser = savedTopic.getReadUsers();
            if(StringArrayConverter.contains(beforeReadUser, userId)) return;
            String[] afterReadUser = StringArrayConverter.add(beforeReadUser, topic.getUserId());

            savedTopic.setReadUsers(afterReadUser);
            savedTopic = topicRepository.save(savedTopic);
            logger.info(process+"[UPDATE]"+savedTopic);

            String roomId = topic.getRoomId();
            Room room = roomRepository.findById(roomId).get();

            if(Topic.Command.SEND.equals(savedTopic.getCommand()) && room.getRoomBadge()-1 >= 0){
                room.setRoomBadge(room.getRoomBadge()-1);
                roomRepository.save(room);
                logger.info(process+"[UPDATE]"+room);
            }

            chatSending.broadcastMessage(savedTopic);

        }catch(Exception e){
            logger.info(process, e);
        }
    }
}
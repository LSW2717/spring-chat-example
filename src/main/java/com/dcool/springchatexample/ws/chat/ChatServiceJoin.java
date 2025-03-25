package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Topic;
import com.dcool.springchatexample.domain.converters.StringArrayConverter;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component("ChatService_JOIN")
public class ChatServiceJoin extends ChatService {

    protected Topic.Command command = Topic.Command.JOIN;

    @Override
    public void process(Topic topic) {

        long timestamp = System.currentTimeMillis();
        String process = "[#"+timestamp+"]: ";
        logger.info(process+topic);

        try{
            String userId = topic.getUserId();
            String roomId = topic.getRoomId();
            Room room = roomRepository.findById(roomId).get();
            Channel channel = room.getChannel();

            String[] beforeJoinUsers = channel.getJoinUsers();
            if(StringArrayConverter.contains(beforeJoinUsers, userId)) return;
            String[] afterJoinUsers = StringArrayConverter.add(beforeJoinUsers, userId);

            topic.setTopicId(timestamp);
            topic.setCommand(Topic.Command.JOIN);
            topic.setIsFirst(channel.getChannelOwner().equals(userId));
            Topic savedTopic = topicRepository.save(topic);
            logger.info(process+"[CREATE]"+savedTopic);

            UUID channelId = topic.getChannelId();
            List<Room> rooms = roomRepository.findByChannel_ChannelId(channelId);
            for(Room r : rooms){
                if(! StringUtils.hasText(r.getFirstTopic())) {
                    r.setFirstTopic(objectMapper.writeValueAsString(savedTopic));
                    roomRepository.save(r);
                    logger.info(process+"[UPDATE]"+r);
                }
            }

            channel.setJoinUsers(afterJoinUsers);
            channelRepository.save(channel);
            logger.info(process+"[UPDATE]"+channel);

            chatSending.broadcastMessage(savedTopic);

        }catch(Exception e){
            logger.info(process, e);
        }
    }

}
package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Topic;
import com.dcool.springchatexample.domain.converters.StringArrayConverter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("ChatService_LEAVE")
public class ChatServiceLeave extends ChatService {

    protected Topic.Command command = Topic.Command.LEAVE;

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

            String[] beforeInviteUsers = channel.getInviteUsers();
            String[] beforeJoinUsers = channel.getJoinUsers();
            if(! StringArrayConverter.contains(beforeJoinUsers, userId)) return;
            String[] afterJoinUsers = StringArrayConverter.remove(beforeJoinUsers, userId);
            String[] afterInviteUsers = StringArrayConverter.remove(beforeInviteUsers, userId);


            if(afterJoinUsers.length != 0) {

                roomRepository.delete(room);
                logger.info(process+"[DELETE]"+room);

                channel.setInviteUsers(afterInviteUsers);
                channel.setJoinUsers(afterJoinUsers);
                channelRepository.save(channel);
                logger.info(process+"[UPDATE]"+room);

                topic.setTopicId(timestamp);
                topic.setCommand(Topic.Command.LEAVE);
                Topic savedTopic = topicRepository.save(topic);
                logger.info(process+"[CREATE]"+savedTopic);

                chatSending.broadcastMessage(savedTopic);

            }else{
                List<Room> rr = roomRepository.findByChannel_ChannelId(channel.getChannelId());
                for(Room r : rr){
                    roomRepository.delete(r);
                    logger.info(process+"[DELETE]"+r);
                }

                topicRepository.deleteByChannelId(channel.getChannelId());
                logger.info(process+"[DELETE] All Topics");

                channelRepository.delete(channel);
                logger.info(process+"[DELETE]"+channel);

            }
        }catch(Exception e){
            logger.info(process, e);
        }
    }
}

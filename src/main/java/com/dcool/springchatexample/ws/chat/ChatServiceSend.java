package com.dcool.springchatexample.ws.chat;

import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Topic;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component("ChatService_SEND")
public class ChatServiceSend extends ChatService{

    protected Topic.Command command = Topic.Command.SEND;

    @Override
    public void process(Topic topic) {

        long timestamp = System.currentTimeMillis();
        String process = "[#" + timestamp + "]: ";
        logger.info(process + topic);

        try {
            UUID channelId = topic.getChannelId();
            List<Room> rooms = roomRepository.findByChannel_ChannelId(channelId);
            Channel channel = rooms.get(0).getChannel();

            topic.setTopicId(timestamp);
            topic.setCommand(command);

            logger.info("Before save: " + topic.getUserId());
            Topic savedTopic = topicRepository.save(topic);
            logger.info("After save: " + savedTopic.getUserId());
            logger.info(process + "[CREATE]" + savedTopic);

            for (Room room : rooms) {
                if (Boolean.FALSE.equals(room.getRoomActive())) {
                    room.setRoomActive(Boolean.TRUE);
                }
                room.setRoomBadge(room.getRoomBadge() + 1);
                roomRepository.save(room);
                logger.info(process + "[UPDATE]" + room);
            }

            channel.setLastTopic(objectMapper.writeValueAsString(savedTopic));
            channelRepository.save(channel);
            logger.info(process + "[UPDATE]" + channel);

            chatSending.broadcastMessage(savedTopic);

        } catch (Exception e) {
            logger.info(process, e);
        }
    }
}

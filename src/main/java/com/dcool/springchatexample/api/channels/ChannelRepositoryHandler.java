package com.dcool.springchatexample.api.channels;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import com.dcool.springchatexample.api.rooms.RoomRepository;
import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.response.BadRequestException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RepositoryEventHandler
public class ChannelRepositoryHandler {

    private Log logger = LogFactory.getLog(getClass());

    private @Autowired ChannelRepository channelRepository;
    private @Autowired RoomRepository roomRepository;

    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    public void handleBefore(Channel entity){
        throw new BadRequestException();
    }


    @HandleBeforeRead
    public void handleBeforeRead(Channel entity, Specification<Channel> specification) {

        if(ObjectUtils.isEmpty(entity.getInviteUsers()))
            throw new BadRequestException();

        Channel channel = process(entity.getChannelOwner(), entity);

        JpaSpecificationBuilder.of(Channel.class)
            .where()
            .and().eq("inviteUsers", channel.getInviteUsers())
            .build(specification);
    }

    @Transactional
    public synchronized Channel process(String owner, Channel entity) {

        List<String> ids = new ArrayList<>();
        ids.add(owner);
        for(String u : entity.getInviteUsers()){
            ids.add(u);
        }
        Collections.sort(ids);

        String[] inviteUsers = new String[ids.size()];
        ids.toArray(inviteUsers);


        Channel channel = channelRepository.findOneByInviteUsers(inviteUsers);
        if(channel == null) {
            channel = new Channel();
            channel.setChannelOwner(owner);
            channel.setInviteUsers(inviteUsers);
            channel.setChannelName(entity.getChannelName());
            channel = channelRepository.save(channel);
            logger.info("New Channel: "+channel);

            for(String userId : ids){
                Room room = new Room();
                room.setRoomId(Room.roomId(userId, channel));
                room.setUserId(userId);
                room.setChannel(channel);
                room.setRoomBadge(0);
                room.setRoomActive(false);
                room.setRoomType("chat");
                room.setRoomNameRef(entity.getChannelName());
                room = roomRepository.save(room);
                logger.info("New Room: "+room);
            }
        }else{
            logger.info("Exists Channel: "+channel);
        }
        return channel;
    }
}

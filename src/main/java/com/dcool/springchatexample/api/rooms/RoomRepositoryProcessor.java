package com.dcool.springchatexample.api.rooms;

import com.dcool.springchatexample.api.friends.FriendRepository;
import com.dcool.springchatexample.api.users.UserRepository;
import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Room.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class RoomRepositoryProcessor  implements RepresentationModelProcessor<EntityModel<Room>> {

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired UserRepository userRepository;
    private @Autowired FriendRepository friendRepository;

    @Override
    public EntityModel<Room> process(EntityModel<Room> model) {

        List<EntityModel<Member>> roomMembers = new ArrayList<>();

        try{
            Room room = model.getContent();
            String userId = room.getUserId();
            Channel channel = room.getChannel();
            String[] inviteUsers = channel.getInviteUsers();

            for(String inviteUser : inviteUsers){

                Room.Member member = new Room.Member();

                friendRepository.findOneByUserIdAndFriend_UserId(userId, inviteUser).ifPresentOrElse((friend->{
                    //Exists Friend With Group
                    member.setType(userId.equals(inviteUser) ? Room.Member.Type.ME : Room.Member.Type.FRIEND);
                    member.setUserId(friend.getFriend().getUserId());
                    member.setUserName(
                        StringUtils.hasText(friend.getFriendName()) ? friend.getFriendName() : friend.getFriend().getUserName());

                }), ()->{

                    userRepository.findById(inviteUser).ifPresentOrElse(user->{
                        member.setType(userId.equals(user.getUserId()) ? Room.Member.Type.ME : Room.Member.Type.NEIGHBOR );
                        member.setUserId(user.getUserId());
                        member.setUserName(user.getUserName());

                    }, ()->{
                        //NoExists User
                        member.setType(Room.Member.Type.STRANGER);
                    });
                });

                roomMembers.add(EntityModel.of(member));
            }

        }catch(Exception e){
            logger.info("", e);
        }
        model.getContent().setRoomMembers(roomMembers);
        return model;
    }
}

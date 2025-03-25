package com.dcool.springchatexample.api.users;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import com.dcool.springchatexample.api.friends.FriendRepository;
import com.dcool.springchatexample.domain.Friend;
import com.dcool.springchatexample.domain.User;
import com.dcool.springchatexample.domain.response.BadRequestException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;


@Component
@RepositoryEventHandler
public class UserRepositoryHandler {

    protected Log logger = LogFactory.getLog(getClass());

    private @Autowired UserRepository userRepository;
    private @Autowired FriendRepository friendRepository;

    @HandleBeforeCreate
    public void handleBeforeCreate(User newUser) {
        if (newUser.getUserName() == null) throw new BadRequestException();
    }

    @HandleAfterCreate
    public void handleAfterCreate(User newUser) {
        List<User> existingUsers = userRepository.findAll();

        for (User existingUser : existingUsers) {
            if (!existingUser.getUserId().equals(newUser.getUserId())) {
                Friend newUserToExistingFriend = new Friend();
                newUserToExistingFriend.setUserId(newUser.getUserId());
                newUserToExistingFriend.setFriend(existingUser);
                newUserToExistingFriend.setFriendName(null);

                Friend existingToNewUserFriend = new Friend();
                existingToNewUserFriend.setUserId(existingUser.getUserId());
                existingToNewUserFriend.setFriend(newUser);
                existingToNewUserFriend.setFriendName(null);

                friendRepository.save(newUserToExistingFriend);
                friendRepository.save(existingToNewUserFriend);
            }
        }
    }

    @HandleBeforeRead
    public void handleBeforeRead(User entity, Specification<User> specification) {

        JpaSpecificationBuilder.of(User.class)
            .where()
            .and().eq("userId", entity.getUserId())
            .build(specification);
    }


}

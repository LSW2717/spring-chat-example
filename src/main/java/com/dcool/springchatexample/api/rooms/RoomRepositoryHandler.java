package com.dcool.springchatexample.api.rooms;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import com.dcool.springchatexample.api.friends.FriendRepository;
import com.dcool.springchatexample.api.users.UserRepository;
import com.dcool.springchatexample.domain.Channel;
import com.dcool.springchatexample.domain.Room;
import com.dcool.springchatexample.domain.Room.Member;
import com.dcool.springchatexample.domain.auditing.AuditedAuditor;
import com.dcool.springchatexample.domain.response.BadRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RepositoryEventHandler
public class RoomRepositoryHandler{

    protected Log logger = LogFactory.getLog(getClass());

    @HandleBeforeCreate
    @HandleBeforeDelete
    public void handleBefore(Room entity) throws Exception{
        throw new BadRequestException();
    }


    @HandleBeforeRead
    public void handleBeforeRead(Room entity, Specification<Room> specification){

        JpaSpecificationBuilder.of(Room.class)
            .where()
            .and().eq("userId", entity.getUserId())
            .and().eq("roomActive", true)
            .build(specification);
    }
}

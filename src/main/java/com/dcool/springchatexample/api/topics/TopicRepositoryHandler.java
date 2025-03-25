package com.dcool.springchatexample.api.topics;

import com.dcool.common.rest.core.annotation.HandleBeforeRead;
import com.dcool.common.rest.jpa.repository.query.JpaSpecificationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import com.dcool.springchatexample.domain.Topic;
import com.dcool.springchatexample.domain.response.BadRequestException;
import org.springframework.util.ObjectUtils;

@Component
@RepositoryEventHandler
public class TopicRepositoryHandler {

    protected Log logger = LogFactory.getLog(getClass());

    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    public void handleBefore(Topic entity){
        throw new BadRequestException();
    }

    @HandleBeforeRead
    public void handleBeforeRead(Topic entity, Specification<Topic> specification){

        if(ObjectUtils.isEmpty(entity.getSearchChannelId()))
            throw new BadRequestException();

        JpaSpecificationBuilder.of(Topic.class)
            .where()
            .and().eq("channelId", entity.getSearchChannelId())
            .and().gt("topicId", entity.getSearchMaxTopicId())
            .and().lt("topicId", entity.getSearchMinTopicId())
            .orderBy().desc("topicId")
            .build(specification);

    }
}

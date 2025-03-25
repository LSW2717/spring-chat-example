package com.dcool.springchatexample.api.topics;

import com.dcool.springchatexample.domain.Topic;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TopicRepository
    extends
    JpaRepository<Topic, Long>,
    JpaSpecificationExecutor<Topic> {

    @Transactional
    void deleteByChannelId(UUID channelId);
}

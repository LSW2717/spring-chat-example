package com.dcool.springchatexample.domain;

import com.dcool.springchatexample.domain.converters.Attributes;
import com.dcool.springchatexample.domain.converters.StringArrayConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.UUID;
import lombok.Data;

@Entity
@Table(
    name = "topics",
    indexes = @Index(name = "topics_idx", columnList = "channelId")
)
@Data
public class Topic {

    @Id
    private Long topicId;

    private UUID channelId;
    private String userId;
    private String userName;

    private Boolean isFirst = false;
    private Command command;
    private String contentType;

    @Column(length = 1024*100)
    private Attributes content;

    @Convert(converter= StringArrayConverter.class)
    @Column(length = 1024*100)
    public String[] readUsers;

    @Transient
    @JsonIgnore
    public String getRoomId(){
        return Room.roomId(userId , channelId);
    }

    public static enum Command {

        // CONNECT,
        // DISCONNECT,
        INVITE, //Rest
        JOIN,  // WS
        LEAVE, //REST
        RECEIVED, //WS
        SEND, //WS
        // SUBSCRIBE,
        // UNSUBSCRIBE,
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Long searchMinTopicId;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Long searchMaxTopicId;


    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public UUID searchChannelId;

    public String toString(){
        return "{topicId="+topicId+", userId="+userId+"}";
    }
}

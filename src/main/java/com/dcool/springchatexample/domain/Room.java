package com.dcool.springchatexample.domain;

import com.dcool.springchatexample.domain.converters.Attributes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.hateoas.EntityModel;

@Entity
@Table(
    name = "rooms",
    indexes = @Index(name = "room_idx", columnList = "userId")
)
@Data
public class Room {
    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roomId;

    private String userId;

    @ManyToOne
    @RestResource(exported = false)
    @JoinColumn(insertable = true, updatable = false, foreignKey=@ForeignKey(value= ConstraintMode.NO_CONSTRAINT))
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Channel channel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer roomBadge;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean roomActive;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roomType;

    @JsonRawValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(length = 1024*100)
    private String firstTopic;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String roomNameRef;

    private String roomName;

    @Column(length = 1024*10)
    private Attributes attributes;


    @Transient
    @JsonIgnore
    public static String roomId(String userId, Channel channel){
        return roomId(userId, channel.getChannelId());
    }
    @Transient
    @JsonIgnore
    public static String roomId(String userId, UUID channelId){
        return userId + "-"+channelId;
    }

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<EntityModel<Member>> roomMembers;

    public @Data static class Member {

        public Type type;
        public String userId;
        public String userName;

        public static enum Type{
            FRIEND,
            ME,
            NEIGHBOR,
            STRANGER,
        }
    }
}

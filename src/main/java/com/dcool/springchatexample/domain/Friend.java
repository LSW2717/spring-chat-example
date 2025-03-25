package com.dcool.springchatexample.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.rest.core.annotation.RestResource;

@Entity
@Table(name = "friends")
@Data
public class Friend {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID friendId;

    private String userId; //Owner...

    @ManyToOne
    @RestResource(exported = false)
    @JoinColumn(name="friendUser" , insertable = true, updatable = false, foreignKey=@ForeignKey(value= ConstraintMode.NO_CONSTRAINT))
    @JsonProperty(access = Access.READ_ONLY)
    private User friend;

    private String friendName;

}

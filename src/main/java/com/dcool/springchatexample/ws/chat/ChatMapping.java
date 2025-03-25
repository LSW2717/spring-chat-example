package com.dcool.springchatexample.ws.chat;

import com.dcool.common.stomp.support.StompHeaderAccessorSupport;
import com.dcool.springchatexample.domain.Topic;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatMapping {

    protected Log logger = LogFactory.getLog(getClass());

    protected @Autowired(required = false) Map<String, ChatService> services;
    protected @Autowired ChatSending chatSending;


    @MessageMapping("chat.{channelId}")
    public void handleMessage(StompHeaderAccessor headers, @Payload Topic m, @DestinationVariable String channelId) {

        m.setChannelId(UUID.fromString(channelId));
//        m.setUserId(m.getUserId());

        logger.info(m.getCommand()+" ["+m.getUserId()+"] ["+m.getChannelId()+"]");

        if(ObjectUtils.isEmpty(services)) {
            chatSending.broadcastMessage(m);
            return;
        }
        ChatService service = services.get("ChatService_"+m.getCommand().name());
        if(ObjectUtils.isEmpty(service)) {
            chatSending.broadcastMessage(m);
            return;
        }
        service.process(m);
    }
}
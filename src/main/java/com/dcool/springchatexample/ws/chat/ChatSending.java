package com.dcool.springchatexample.ws.chat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import com.dcool.springchatexample.domain.Topic;

@Component
public class ChatSending {

    protected Log logger = LogFactory.getLog(getClass());

    protected @Autowired SimpMessageSendingOperations sendingOperations;

    public void broadcastMessage(Topic m){
        String destination = "/topic/chat."+m.getChannelId();
        sendingOperations.convertAndSend(destination, m);
        logger.info("BRODCAST ["+ ClassUtils.getShortName(getClass())+"] ["+m.getChannelId()+"]");
    }
}

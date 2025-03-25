package com.dcool.springchatexample;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class ApplicationStompSecurityConfig
    extends AbstractSecurityWebSocketMessageBrokerConfigurer
    implements ChannelInterceptor {

    protected Log logger = LogFactory.getLog(getClass());

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry message) {

        message
            .nullDestMatcher().permitAll()
//                .simpDestMatchers("/app/**").authenticated()
//                .simpSubscribeDestMatchers("/topic/**").authenticated()
            .simpDestMatchers("/app/**").permitAll()
            .simpSubscribeDestMatchers("/topic/**").permitAll()
//                .anyMessage().permitAll();
            .anyMessage().denyAll();
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }


    @Autowired
    protected JwtDecoder jwtDecoder;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        Authentication auth = (Authentication) accessor.getUser();
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        StompCommand command = accessor.getCommand();
//        List<String> authorization1 = accessor.getNativeHeader("Authorization");
//        Object authorization2 = accessor.getHeader("Authorization");
//        Authentication a = SecurityContextHolder.getContext().getAuthentication();
//        Authentication b = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
//        Principal principal = accessor.getUser();
//        logger.info(command+": " + principal);
        return message;
    }
}

package com.dcool.springchatexample;


import com.dcool.common.stomp.support.StompBrokerRelayProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class ApplicationStompConfig implements WebSocketMessageBrokerConfigurer{

    protected Log logger = LogFactory.getLog(getClass());

    public static final String WS_CONNECTION    = "/connect";  //sockjs '/connect/websocket'
    public static final String WS_BROADCASTING  = "/app/";
    public static final String WS_SUBSCRIPTIONS = "/topic/";


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WS_CONNECTION)
//                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOriginPatterns("*").withSockJS();

//        registry.setErrorHandler(new ChatErrorHandler());
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(10 * 1024);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setApplicationDestinationPrefixes(WS_BROADCASTING); //app

        ///////////////////////////////////////////
        // Enables a simple in-memory broker
        ///////////////////////////////////////////
//        if(! stompBrokerRelayProperties().available()) {
//            logger.info("Enable Simple Broker");
//            registry.enableSimpleBroker(WS_SUBSCRIPTIONS); //topic
//            return;
//        }
        registry.enableSimpleBroker(WS_SUBSCRIPTIONS); //topic

        ///////////////////////////////////////////
        // Enable STOMP MessageQueue broker
        ///////////////////////////////////////////
//        logger.info("Enable Stomp Broker Relay");
//        registry.enableStompBrokerRelay(WS_SUBSCRIPTIONS)//topic
//                .setRelayHost(stompBrokerRelayProperties().getHost())
//		        .setRelayPort(stompBrokerRelayProperties().getPort())
//                .setSystemLogin(stompBrokerRelayProperties().getUsername())
//                .setSystemPasscode(stompBrokerRelayProperties().getPassword())
//		        .setClientLogin(stompBrokerRelayProperties().getUsername())
//		        .setClientPasscode(stompBrokerRelayProperties().getPassword())
//        ;
    }

    @Bean
    protected StompBrokerRelayProperties stompBrokerRelayProperties(){
        return new StompBrokerRelayProperties();
    };

    @Autowired
    private ApplicationStompSecurityConfig applicationStompSecurityConfig;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(applicationStompSecurityConfig);
    }

}

package konkuk.nServer.domain.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ws://localhost:8080/ws/chat
        registry.addEndpoint("/ws/chat") // 웹소켓에 접속하기 위한 endpoint
                .setAllowedOriginPatterns("*") // CORS 정책
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 받을 때 관련 경로 설정
        // "/sub"이  prefix(api 경로 맨 앞)에 붙은 경우, messageBroker가 잡아서 해당 채팅방을 구독하고 있는 클라이언트에게 메시지를 전달해줌
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub"); // 메시지 보낼 때 관련 경로 설정
        // 클라이언트가 메시지를 보낼 때 경로 맨앞에 "/pub"이 붙어있으면 Broker로 보내짐.
    }

    // RabbitMQ 설정
    /*
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub")
                .enableStompBrokerRelay("/topic")
                .setRelayHost("localhost")
                .setVirtualHost("/")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
    }*/
}

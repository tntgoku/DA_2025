package backend.main.Config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import backend.main.DTO.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebEventListener {
    private final SimpMessageSendingOperations messagingTemplate;

    // @EventListener
    // public void
    // handleWebSocketConnectListener(org.springframework.web.socket.messaging.SessionConnectedEvent
    // event) {
    // StompHeaderAccessor headerAccessor =
    // StompHeaderAccessor.wrap(event.getMessage());
    // String username = (String)
    // headerAccessor.getSessionAttributes().get("username");
    // if (username != null) {
    // log.info("Received a new web socket connection");
    // var chatMessage = ChatMessage.builder()
    // .type(backend.main.DTO.MessageType.JOIN)
    // .sender(username)
    // .build();
    // messagingTemplate.convertAndSend("/topic/messages", chatMessage);
    // }
    // }

    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event) {
        log.info("Web socket connection closed");
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() != null) {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            if (username != null) {
                var chatMessage = ChatMessage.builder()
                        .type(backend.main.DTO.MessageType.LEAVE)
                        .sender(username)
                        .build();
                messagingTemplate.convertAndSend("/topic/messages", chatMessage);
            }
        }
    }
}
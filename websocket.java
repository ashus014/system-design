//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
//@ServerEndpoint("/websocket")
//public class websocket {
//    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
//
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("WebSocket connection opened: " + session.getId());
//        sessions.add(session);
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("Message received from " + session.getId() + ": " + message);
//        // Broadcast the message to all connected clients
//        broadcast(message);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println("WebSocket connection closed: " + session.getId());
//        sessions.remove(session);
//    }
//
//    @OnError
//    public void onError(Throwable error) {
//        System.err.println("WebSocket error: " + error.getMessage());
//    }
//
//    private void broadcast(String message) {
//        sessions.forEach(session -> {
//            try {
//                session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                System.err.println("Error broadcasting message: " + e.getMessage());
//            }
//        });
//    }
//}

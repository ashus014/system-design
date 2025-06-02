import { WebSocketServer } from "ws";

// Create a WebSocket server instance
const wss = new WebSocketServer({ port: 8080 });

// Store rooms and their clients
const rooms = new Map();

// Connection handler
wss.on("connection", (ws) => {
  console.log("New client connected");

  // Send welcome message
  ws.send(
    JSON.stringify({
      type: "welcome",
      message: "Connected to WebSocket server",
    })
  );

  // Message handler
  ws.on("message", (message) => {
    try {
      const data = JSON.parse(message);
      console.log("Received:", data);

      switch (data.type) {
        case "join":
          handleJoinRoom(ws, data.roomId);
          break;
        case "leave":
          handleLeaveRoom(ws, data.roomId);
          break;
        case "message":
          handleRoomMessage(ws, data);
          break;
        default:
          console.log("Unknown message type:", data.type);
      }
    } catch (error) {
      console.error("Error processing message:", error);
    }
  });

  // Close handler
  ws.on("close", () => {
    // Remove client from all rooms
    rooms.forEach((clients, roomId) => {
      if (clients.has(ws)) {
        handleLeaveRoom(ws, roomId);
      }
    });
    console.log("Client disconnected");
  });

  // Error handler
  ws.on("error", (error) => {
    console.error("WebSocket error:", error);
    // Remove client from all rooms
    rooms.forEach((clients, roomId) => {
      if (clients.has(ws)) {
        handleLeaveRoom(ws, roomId);
      }
    });
  });
});

// Handle joining a room
function handleJoinRoom(ws, roomId) {
  if (!rooms.has(roomId)) {
    rooms.set(roomId, new Set());
  }

  const roomClients = rooms.get(roomId);
  roomClients.add(ws);

  // Notify client
  ws.send(
    JSON.stringify({
      type: "room_joined",
      roomId: roomId,
      message: `Joined room: ${roomId}`,
    })
  );

  // Notify other clients in the room
  broadcastToRoom(
    roomId,
    JSON.stringify({
      type: "user_joined",
      roomId: roomId,
      message: "New user joined the room",
    }),
    ws
  );

  console.log(`Client joined room: ${roomId}`);
}

// Handle leaving a room
function handleLeaveRoom(ws, roomId) {
  const roomClients = rooms.get(roomId);
  if (roomClients) {
    roomClients.delete(ws);

    // Notify other clients in the room
    broadcastToRoom(
      roomId,
      JSON.stringify({
        type: "user_left",
        roomId: roomId,
        message: "User left the room",
      }),
      ws
    );

    // Remove room if empty
    if (roomClients.size === 0) {
      rooms.delete(roomId);
    }
  }
  console.log(`Client left room: ${roomId}`);
}

// Handle room messages
function handleRoomMessage(ws, data) {
  const { roomId, message } = data;
  if (rooms.has(roomId)) {
    broadcastToRoom(
      roomId,
      JSON.stringify({
        type: "message",
        roomId: roomId,
        message: message,
      })
    );
  }
}

// Broadcast message to all clients in a specific room
function broadcastToRoom(roomId, message, excludeClient = null) {
  const roomClients = rooms.get(roomId);
  if (roomClients) {
    roomClients.forEach((client) => {
      if (
        client !== excludeClient &&
        client.readyState === WebSocketServer.OPEN
      ) {
        client.send(message);
      }
    });
  }
}

console.log("WebSocket server is running on port 8080");

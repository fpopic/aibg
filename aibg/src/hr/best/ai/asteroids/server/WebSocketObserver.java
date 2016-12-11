package hr.best.ai.asteroids.server;

import com.google.gson.JsonObject;
import hr.best.ai.gl.NewStateObserver;
import hr.best.ai.gl.State;
import org.apache.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.ClosedByInterruptException;

/**
 * Created by lpp on 12/8/16.
 */
public class WebSocketObserver extends WebSocketServer implements NewStateObserver {
    final static Logger logger = Logger.getLogger(WebSocketObserver.class);

    
    public WebSocketObserver(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public WebSocketObserver(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected to socket");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " disconnected to socket");
    }

    @Override
    public void start() {
        super.start();
        logger.info("Started websocket at " + this.getAddress() + ":" + this.getPort());
    }

    @Override
    public void stop(int timeout) throws IOException, InterruptedException {
        logger.info("Stopping websocket at " + this.getAddress() + ":" + this.getPort());
        super.stop(timeout);
        logger.info("Stopped websocket at " + this.getAddress() + ":" + this.getPort());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
    	logger.info("Received message " + this.getName() + ":: " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (ex instanceof ClosedByInterruptException) {
            logger.debug("Ignoring ClosedByInterruptException; happens at closing websocket", ex);
            return;
        }
        ex.printStackTrace();
        logger.error(ex.getMessage());
        if (conn != null) {
            logger.error("Connection responsible" + conn);
        }
    }

    @Override
    public void signalNewState(State state) {
    	sendToConnections(state.toJSONObject().toString());
    	logger.info("Sent state: "+ state.toJSONObject().toString());
    }
    
    private void sendToConnections(String message){
    	for (WebSocket sock : this.connections()) {
            sock.send(message);
        }
    }

    @Override
    public void signalError(JsonObject jsonObject) {
        for (WebSocket sock : this.connections()) {
            sock.send(jsonObject.toString());
        }
        try {
            this.close();
        } catch (Exception ignore) {
        }
    }

    @Override
    public String getName() {
        return "Websocket observer [" + this.getAddress() + ":" + this.getPort() + "]";
    }

    @Override
    public void close() throws Exception {
        this.connections().forEach(WebSocket::close);
    }
}

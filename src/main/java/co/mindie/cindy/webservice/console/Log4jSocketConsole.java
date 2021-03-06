/////////////////////////////////////////////////
// Project : WSFramework
// Package : co.mindie.wsframework.console
// SocketConsole.java
//
// Author : Simon CORSIN <simoncorsin@gmail.com>
// File created on Jun 30, 2014 at 12:57:18 PM
////////

package co.mindie.cindy.webservice.console;

import co.mindie.cindy.webservice.app.CindyWebApp;
import co.mindie.cindy.core.annotation.Load;
import co.mindie.cindy.core.annotation.Wired;
import co.mindie.cindy.webservice.configuration.Configuration;
import co.mindie.cindy.core.exception.CindyException;
import co.mindie.cindy.core.tools.Initializable;
import me.corsin.javatools.io.IOUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Load(creationPriority = -1)
public class Log4jSocketConsole extends AbstractAppender implements IConnectionListener, Initializable {

	////////////////////////
	// VARIABLES
	////////////////

	private static final Logger LOGGER = Logger.getLogger(Log4jSocketConsole.class);
	private ServerSocket serverSocket;
	private final List<Connection> connections;
	private boolean closed;

	@Wired private Configuration configuration;
	@Wired private CindyWebApp application;

	////////////////////////
	// CONSTRUCTORS
	////////////////

	public Log4jSocketConsole() {
		this.connections = new ArrayList<>();
	}

	////////////////////////
	// METHODS
	////////////////

	public IConsoleProtocol createProtocol(Connection connection) {
		return new InterpreterProtocol(connection);
	}

	@Override
	public void init() {
		Integer port = this.configuration.getInteger("cindy.socket_console_port");

		if (port != null) {
			try {
				this.serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				throw new CindyException("Failed to init SocketConsole socket", e);
			}

			Thread thread = new Thread(Log4jSocketConsole.this::accept, "SocketConsole");

			thread.start();
			LOGGER.trace("Enabled Log4jSocketConsole on port " + port);
 		}
	}

	private void accept() {
		while (!this.closed) {
			try {
				final Socket socket = this.serverSocket.accept();
				final Connection connection = new Connection(this.application, socket);
				connection.setListener(this);
				connection.setProtocol(this.createProtocol(connection));

				LOGGER.info(socket.getRemoteSocketAddress() + " is now listening to console");

				this.addConnection(connection);
			} catch (IOException ignored) {
			}
		}
	}

	private void sendMessage(String message, Level level) {
		for (Connection connection : this.connections) {
			connection.send(message, level);
		}
	}

	private void addConnection(Connection connection) {
		synchronized (this.connections) {
			this.connections.add(connection);
		}

		connection.markReady();
	}

	private void closeConnection(Connection connection) {
		LOGGER.info(connection.getSocket().getRemoteSocketAddress() + " is now disconnected from console");

		synchronized (this.connections) {
			this.connections.remove(connection);
			connection.setListener(null);
			connection.close();
		}
	}

	@Override
	public void doAppend(LoggingEvent event) {
		if (this.serverSocket != null) {
			final String renderedMessage = new DateTime(event.getTimeStamp()).toString("yyyy-MM-dd HH:mm:ss,SSS")
					+ " [" + event.getLevel().toString() + "]"
					+ " (" + event.getLocationInformation().getClassName() + ":" + event.getLocationInformation().getLineNumber() + ") "
					+ event.getRenderedMessage() + "\n";

			this.sendMessage(renderedMessage, event.getLevel());
		}
	}

	@Override
	public void close() {
		this.closed = true;
		synchronized (this.connections) {
			for (Connection connection : this.connections) {
				connection.setListener(null);
				connection.close();
			}
		}
		this.connections.clear();

		IOUtils.closeStream(this.serverSocket);
		this.serverSocket = null;
	}

	@Override
	public void onDisconnected(Connection connection, Exception e) {
		this.closeConnection(connection);
	}

	////////////////////////
	// GETTERS/SETTERS
	////////////////

}

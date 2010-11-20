package net.aeten.core.messenger;

import java.io.IOException;

import net.aeten.core.Connection;
import net.aeten.core.Identifiable;
import net.aeten.core.messenger.args4j.ReceiverOptionHandler;

import org.kohsuke.args4j.CmdLineParser;

public interface Receiver<Message> extends Identifiable, Connection {
	public void receive(MessengerEventData<Message> data) throws IOException;
	
	public static abstract class Helper<Message> extends net.aeten.core.messenger.Helper implements Receiver<Message> {
		static {
			CmdLineParser.registerHandler(Receiver.class, ReceiverOptionHandler.class);
		}
		public Helper() {
			super();
		}
		public Helper(String identifier) {
			super(identifier);
		}
		@Override
		public String toString() {
			return "Receiver \"" + this.getIdentifier() + "\"";
		}
	}
}

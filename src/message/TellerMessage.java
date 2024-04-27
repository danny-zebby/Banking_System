package message;
import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class TellerMessage implements MessageInterface, Serializable {
	final Status status;
	final TellerMessageType type;
	final Map<String, String> info;
	final List<String> logs;
	
	// use for add teller
	public TellerMessage(Status status, String name, String password) {
		this.status = status;
		this.type = TellerMessageType.ADD_TELLER;
		this.info = new HashMap<>();
		this.info.put("name", name);
		this.info.put("password", password);
		this.logs = null;
	}
	
	// use for delete teller
	public TellerMessage(Status status, int tellerId) {
		this.status = status;
		this.type = TellerMessageType.REM_TELLER;
		this.info = new HashMap<>();
		this.info.put("tellerId", Integer.toString(tellerId));
		this.logs = null;
	}
	
	public TellerMessage(Status status, TellerMessageType type) {
		this.status = status;
		this.type = type;
		this.info = null;
		this.logs = null;
	}
	
	public TellerMessage(Status status, TellerMessageType type, Map<String, String> info) {
		this.status = status;
		this.type = type;
		this.info = info;
		this.logs = null;
	}
	
	// use for view logs
	public TellerMessage(Status status, List<String> logs) {
		this.status = status;
		this.type = TellerMessageType.VIEW_LOGS;
		this.info = null;
		this.logs = logs;
	}
	
	@Override
	public Status getStatus() {
		return this.status;
	}
	
	public TellerMessageType getType() {
		return this.type;
	}
	
	public Map<String, String> getInfo() {
		return this.info;
	}
	
	public List<String> getLogs() {
		return this.logs;
	}
}

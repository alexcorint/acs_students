package basenostates;

import basenostates.doorstate.DoorState;
import basenostates.doorstate.Unlocked;
import basenostates.requests.RequestReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Context class in the State design pattern.
// A Door represents a physical entry point connecting two spaces: fromSpace (origin,
// where the reader is located) and toSpace (destination, space being accessed).
// It delegates action handling (open, close, lock, unlock, unlockShortly) to its
// current DoorState instance, allowing dynamic state changes at runtime.
public class Door {
  private final String id;
  private final String fromSpace;
  private final String toSpace;
  private boolean closed; // physically
  private DoorState state;

  private static final Logger log = LoggerFactory.getLogger(Door.class);

  public Door(String id, String fromSpace, String toSpace) {
    this.id = id;
    this.fromSpace = fromSpace;
    this.toSpace = toSpace;
    this.state = new Unlocked(this);
    this.closed = true;
  }

  public void processRequest(RequestReader request) {
    if (request.isAuthorized()) {
      String action = request.getAction();
      doAction(action);
    } else {
      log.debug("Not enough permissions to perform action '{}' on door '{}'.",
          request.getAction(), id);
    }
    request.setDoorStateName(getStateName());
  }

  private void doAction(String action) {
    switch (action) {
      case Actions.UNLOCK:
        state.unlock();
        break;
      case Actions.OPEN:
        state.open();
        break;
      case Actions.LOCK:
        state.lock();
        break;
      case Actions.CLOSE:
        state.close();
        break;
      case Actions.UNLOCK_SHORTLY:
        state.unlockShortly();
        break;
      default:
        assert false : "Unknown action: " + action;
        System.exit(-1);
    }
  }

  public boolean isClosed() {
    return closed;
  }

  public String getId() {
    return id;
  }

  public String getFromSpace() {
    return fromSpace;
  }

  public String getToSpace() {
    return toSpace;
  }

  public String getSpace() {
    return toSpace;
  }

  public String getStateName() {
    return state.toString();
  }

  public void setState(DoorState state) {
    log.info("[DOOR] Door '{}' changed to '{}'", id, state.toString());
    this.state = state;
  }

  public void setClosed(boolean closed) {
    log.info("[DOOR] Door '{}' changed to '{}'", id, (closed ? "closed" : "open"));
    this.closed = closed;
  }

  @Override
  public String toString() {
    return "Door{"
        + "id='" + id + '\''
        + ", fromSpace='" + fromSpace + '\''
        + ", toSpace='" + toSpace + '\''
        + ", closed=" + closed
        + ", state=" + getStateName()
        + "}";
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("originSpace", fromSpace);
    json.put("destinationSpace", toSpace);
    json.put("state", getStateName());
    json.put("closed", closed);
    return json;
  }
}

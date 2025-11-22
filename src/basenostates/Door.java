package basenostates;

import basenostates.doorstate.DoorState;
import basenostates.doorstate.Unlocked;
import basenostates.requests.RequestReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DOOR CLASS.
 * Represents a physical door in a logical way to control
 * its states.
 */
public class Door {
  private final String id;
  private boolean closed; // physically
  private DoorState state;

  private static final Logger log = LoggerFactory.getLogger(Door.class);

  /**
   * Constructor for the Door class.
   * @param id the identification string of the door.
   */
  public Door(String id) {
    this.id = id;
    this.state = new Unlocked(this);
    this.closed = true;
  }

  /**
   * Processes the order sent from a Request Reader.
   * @param request The instance of RequestReader to be
   *                processed.
   */
  public void processRequest(RequestReader request) {
    // it is the Door that process the request because the door has and knows
    // its state, and if closed or open
    if (request.isAuthorized()) {
      String action = request.getAction();
      doAction(action);
    } else {
      log.warn("Not enough permissions to perform this operation.");
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

  /**
   * Returns whether the door is closed or not.
   * @return true if the door is closed.
   */
  public boolean isClosed() {
    return closed;
  }

  /**
   * Returns the identification of the door.
   * @return ID string.
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the name of the actual door state.
   * @return Door State string.
   */
  public String getStateName() {
    return state.toString();
  }

  /**
   * Sets the actual state of the door.
   * @param state DoorState instance of the actual
   *              state.
   */
  public void setState(DoorState state) {
    this.state = state;
  }

  /**
   * Sets if the door is closed or not.
   * @param closed boolean of the closed state.
   */
  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  /**
   * Converts the door data into a string.
   * @return String 'Door{id, closed, state}'
   */
  @Override
  public String toString() {
    return "Door{"
        + "id='" + id + '\''
        + ", closed=" + closed
        + ", state=" + getStateName()
        + "}";
  }

  /**
   * Converts the door data into a JSON object.
   * @return JSON object of the instanced door.
   */
  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", getStateName());
    json.put("closed", closed);
    return json;
  }
}

package baseNoStates;

import baseNoStates.requests.RequestReader;
import org.json.JSONObject;

// the door does the actions trough its state (State pattern, see DoorState).
// fromSpace is where the reader is and toSpace where you go in, we need them for the permisions
public class Door {
  private final String id;
  private boolean closed; // physically
  private DoorState state;
  private final Space fromSpace;
  private final Space toSpace;

  public Door(String id, Space fromSpace, Space toSpace) {
    this.id = id;
    this.fromSpace = fromSpace;
    this.toSpace = toSpace;
    closed = true;
    state = new Unlocked(this); // at the begining all closed and unlocked
    toSpace.addDoor(this); // so the space knows its doors
  }

  public void processRequest(RequestReader request) {
    // it is the Door that process the request because the door has and knows
    // its state, and if closed or open
    if (request.isAuthorized()) {
      String action = request.getAction();
      doAction(action);
    } else {
      System.out.println("not authorized");
    }
    request.setDoorStateName(getStateName());
  }

  private void doAction(String action) {
    // now the state decides what to do
    switch (action) {
      case Actions.OPEN:
        state.open();
        break;
      case Actions.CLOSE:
        state.close();
        break;
      case Actions.LOCK:
        state.lock();
        break;
      case Actions.UNLOCK:
        state.unlock();
        break;
      case Actions.UNLOCK_SHORTLY:
        state.unlockShortly();
        break;
      default:
        assert false : "Unknown action " + action;
        System.exit(-1);
    }
  }

  // the states call it to change
  public void setState(DoorState state) {
    System.out.println("door " + id + " : " + this.state.getName() + " -> " + state.getName());
    this.state = state;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  public boolean isClosed() {
    return closed;
  }

  public String getId() {
    return id;
  }

  public String getStateName() {
    //return "unlocked";
    return state.getName();
  }

  public Space getFromSpace() {
    return fromSpace;
  }

  public Space getToSpace() {
    return toSpace;
  }

  @Override
  public String toString() {
    return "Door{"
        + "id='" + id + '\''
        + ", closed=" + closed
        + ", state=" + getStateName()
        + ", from=" + fromSpace.getId()
        + ", to=" + toSpace.getId()
        + "}";
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", getStateName());
    json.put("closed", closed);
    // com a l'annex A de l'enunciat
    json.put("originSpace", fromSpace.getId());
    json.put("destinationSpace", toSpace.getId());
    return json;
  }
}

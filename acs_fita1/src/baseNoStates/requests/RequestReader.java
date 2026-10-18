package baseNoStates.requests;

import baseNoStates.DirectoryAreas;
import baseNoStates.DirectoryUsers;
import baseNoStates.Door;
import baseNoStates.User;
import baseNoStates.UserGroup;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestReader implements Request {
  private final String credential; // who
  private final String action;     // what
  private final LocalDateTime now; // when
  private final String doorId;     // where
  private String userName;
  private boolean authorized;
  private final ArrayList<String> reasons; // why not authorized
  private String doorStateName;
  private boolean doorClosed;

  public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
    this.credential = credential;
    this.action = action;
    this.doorId = doorId;
    reasons = new ArrayList<>();
    this.now = now;
  }

  public void setDoorStateName(String name) {
    doorStateName = name;
  }

  public String getAction() {
    return action;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void addReason(String reason) {
    reasons.add(reason);
  }


  @Override
  public String toString() {
    if (userName == null) {
      userName = "unknown";
    }
    return "Request{"
            + "credential=" + credential
            + ", userName=" + userName
            + ", action=" + action
            + ", now=" + now
            + ", doorID=" + doorId
            + ", closed=" + doorClosed
            + ", authorized=" + authorized
            + ", reasons=" + reasons
            + "}";
  }

  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("authorized", authorized);
    json.put("action", action);
    json.put("doorId", doorId);
    json.put("closed", doorClosed);
    json.put("state", doorStateName);
    json.put("reasons", new JSONArray(reasons));
    return json;
  }

  // see if the request is authorized and put this into the request, then send it to the door.
  // if authorized, perform the action.
  public void process() {
    User user = DirectoryUsers.findUserByCredential(credential);
    if (user != null) {
      userName = user.getName(); // for the toString
    }
    Door door = DirectoryAreas.findDoorById(doorId);
    assert door != null : "door " + doorId + " not found";
    authorize(user, door);
    // this sets the boolean authorize attribute of the request
    door.processRequest(this);
    // even if not authorized we process the request, so that if desired we could log all
    // the requests made to the server as part of processing the request
    doorClosed = door.isClosed();
  }

  // the result is put into the request object plus, if not authorized, why not,
  // only for testing
  private void authorize(User user, Door door) {
    if (user == null) {
      authorized = false;
      addReason("user doesn't exists");
    } else {
      // who, what, when and where. We dont stop at the first one so we get all the reasons
      UserGroup group = user.getGroup();
      // System.out.println("authorize " + userName + " group " + group.getName());
      authorized = true;
      if (!group.isInSchedule(now)) {
        authorized = false;
        addReason("Present date and/or time not in schedule for group " + group.getName());
      }
      if (!group.canDoAction(action)) {
        authorized = false;
        addReason("Not allowed action " + action + " for group " + group.getName());
      }
      // he has to be allowed in the from space and in the to space
      if (!group.canBeInSpace(door.getFromSpace())) {
        authorized = false;
        addReason("Not allowed origin " + door.getFromSpace().getId()
            + " for group " + group.getName());
      }
      if (!group.canBeInSpace(door.getToSpace())) {
        authorized = false;
        addReason("Not allowed destination " + door.getToSpace().getId()
            + " for group " + group.getName());
      }
    }
  }
}

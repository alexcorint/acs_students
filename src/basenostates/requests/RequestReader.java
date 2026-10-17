package basenostates.requests;

import basenostates.AuthGroup;
import basenostates.DirectoryDoors;
import basenostates.DirectoryUsers;
import basenostates.Door;
import basenostates.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Represents an action request initiated from an individual door's credential reader.
// Encapsulates the four Ws (Who: credential, What: action, Where: doorId, When: now).
// Authorizes the request using the user's AuthGroup and, if authorized, delegates the
// execution to the corresponding Door.
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
  private static final Logger log = LoggerFactory.getLogger(RequestReader.class);

  public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
    this.credential = credential;
    this.action = action;
    this.doorId = doorId;
    this.reasons = new ArrayList<>();
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

  public String getUserName() {
    return userName != null ? userName : "unknown";
  }

  public String getDoorId() {
    return doorId;
  }

  public LocalDateTime getNow() {
    return now;
  }

  public ArrayList<String> getReasons() {
    return reasons;
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

  public void process() {
    User user = DirectoryUsers.getInstance().findUserByCredential(credential);
    Door door = DirectoryDoors.getInstance().findDoorById(doorId);

    if (door == null) {
      log.warn("[REQUEST] FAILED: Door '{}' not found for user '{}'", doorId, credential);
      return;
    }

    authorize(user, door);

    String preState = door.getStateName();
    boolean preClosed = door.isClosed();

    door.processRequest(this);

    if (isAuthorized()) {
      if (!preState.equals(door.getStateName()) || preClosed != door.isClosed()) {
        log.info("[REQUEST] SUCCESS: Authorized for user '{}' ({})",
            userName, (user != null ? user.getRole() : "unknown"));
      } else {
        log.debug("[REQUEST] Action ignored or redundant.");
      }
    } else {
      log.warn("[REQUEST] DENIED: User {} forbidden on door '{}'. Reasons: {}",
          userName, doorId, reasons);
    }

    doorClosed = door.isClosed();
  }

  private void authorize(User user, Door door) {
    if (user == null) {
      authorized = false;
      addReason("User doesn't exist");
    } else {
      this.userName = user.getName();
      AuthGroup group = user.getAuthGroup();

      if (group == null) {
        authorized = false;
        addReason("User has no permissions assigned");
      } else {
        ArrayList<String> failedReasons = group.checkPermissions(action, door, now, user.getRole());
        if (failedReasons.isEmpty()) {
          authorized = true;
        } else {
          authorized = false;
          for (String r : failedReasons) {
            addReason(r);
          }
        }
      }
    }
  }
}

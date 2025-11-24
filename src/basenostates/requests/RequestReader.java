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

/**
 * REQUEST READER CLASS.
 * Processes the request (Who, What, Where, When) to decide
 * if an action is authorized.
 */
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

  /**
   * Constructor for the RequestReader.
   *
   * @param credential The user credential identifier.
   * @param action The action to perform (e.g., "unlock").
   * @param now The timestamp of the request.
   * @param doorId The identifier of the target door.
   */
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

  /**
   * Converts the request result to a String.
   * @return String representation of the request.
   */
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

  /**
   * Converts the answer to a JSON object.
   * @return JSONObject with the result data.
   */
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

  /**
   * Processes the request: finds user/door and checks authorization.
   * If authorized, delegates the action to the door.
   */
  public void process() {
    User user = DirectoryUsers.findUserByCredential(credential);
    Door door = DirectoryDoors.findDoorById(doorId);

    if (door == null) {
      log.warn("[REQUEST] FAILED: Door '{}' not found for user '{}'",
          doorId, credential);
      return;
    }

    authorize(user, door);

    String preState = door.getStateName();
    boolean preClosed = door.isClosed();

    door.processRequest(this);

    if (isAuthorized()) {
      if (!preState.equals(door.getStateName())
          || preClosed != door.isClosed()) {
        log.info("[REQUEST] SUCCESS: Authorized for user '{}' ({})",
            userName, (user != null ? user.getRole() : "unknown"));
      } else {
        log.debug("[REQUEST] Action ignored or redundant.");
      }

    } else {
      log.warn("[REQUEST] DENIED: User {} forbidden on  door '{}'. Reason: {}",
          userName, doorId, reasons);
    }

    doorClosed = door.isClosed();
  }

  /**
   * Internal method to check logic rules against AuthGroup.
   *
   * @param user The user attempting the action.
   * @param door The target door.
   */
  private void authorize(User user, Door door) {
    if (user == null) {
      authorized = false;
      addReason("User doesn't exists");
    } else {
      this.userName = user.getName();

      AuthGroup group = user.getAuthGroup();

      if (group == null) {
        authorized = false;
        addReason("User has no permissions assigned");
      } else {
        authorized = group.isAllowed(action, doorId, now);

        if (!authorized) {
          addReason("User is not authorized to perform action '" + action + "' on door '"
              + doorId + "' at this time.");
        }
      }
    }
  }
}


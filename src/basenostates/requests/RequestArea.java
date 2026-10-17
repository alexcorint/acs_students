package basenostates.requests;

import basenostates.AccessDoors;
import basenostates.Actions;
import basenostates.Area;
import basenostates.DirectoryAreas;
import basenostates.DirectoryUsers;
import basenostates.Door;
import basenostates.User;
import basenostates.Visitor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Represents an action request applied to an entire area (Partition or Space).
// Only 'lock' and 'unlock' actions are allowed for areas.
// Processing the request uses the AccessDoors visitor to discover all doors in the area,
// then transforms the request into individual RequestReader calls for each door.
public class RequestArea implements Request {
  private final String credential;
  private final String action;
  private final String areaId;
  private final LocalDateTime now;
  private final ArrayList<RequestReader> requests = new ArrayList<>();
  private static final Logger log = LoggerFactory.getLogger(RequestArea.class);

  public RequestArea(String credential, String action, LocalDateTime now, String areaId) {
    this.credential = credential;
    this.areaId = areaId;
    assert action.equals(Actions.LOCK) || action.equals(Actions.UNLOCK)
        : "invalid action " + action + " for an area request";
    this.action = action;
    this.now = now;
  }

  public String getAction() {
    return action;
  }

  public String getAreaId() {
    return areaId;
  }

  public LocalDateTime getNow() {
    return now;
  }

  public String getUserName() {
    User user = DirectoryUsers.getInstance().findUserByCredential(credential);
    return user != null ? user.getName() : "unknown";
  }

  public boolean isAuthorized() {
    for (RequestReader r : requests) {
      if (!r.isAuthorized()) {
        return false;
      }
    }
    return !requests.isEmpty();
  }

  @Override
  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("action", action);
    json.put("areaId", areaId);
    JSONArray jsonRequests = new JSONArray();
    for (RequestReader rd : requests) {
      jsonRequests.put(rd.answerToJson());
    }
    json.put("requestsDoors", jsonRequests);
    return json;
  }

  @Override
  public String toString() {
    String requestsDoorsStr;
    if (requests.isEmpty()) {
      requestsDoorsStr = "";
    } else {
      requestsDoorsStr = requests.toString();
    }
    return "Request{"
        + "credential=" + credential
        + ", action=" + action
        + ", now=" + now
        + ", areaId=" + areaId
        + ", requestsDoors=" + requestsDoorsStr
        + "}";
  }

  public void process() {
    Area area = DirectoryAreas.getInstance().findArea(areaId);

    if (area != null) {
      Visitor accessDoorsVisitor = new AccessDoors();
      area.acceptVisitor(accessDoorsVisitor);
      ArrayList<Door> doors = accessDoorsVisitor.getDoors();

      for (Door door : doors) {
        RequestReader requestReader = new RequestReader(credential, action, now, door.getId());
        requestReader.process();
        requests.add(requestReader);
      }
    } else {
      log.warn("No area found with id: '{}'", areaId);
    }
  }
}

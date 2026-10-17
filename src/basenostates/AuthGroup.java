package basenostates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Represents the set of access rights granted to a role in the RBAC system.
// It bundles:
// 1. WHAT: allowed actions (open, close, lock, unlock, unlock_shortly)
// 2. WHERE: allowed areas (Partitions or Spaces)
// 3. WHEN: allowed schedule (date range, time range, days of the week)
public class AuthGroup {
  private final ArrayList<String> actions;
  private final ArrayList<Area> allowedAreas;
  private final Schedule schedule;
  private final Logger log = LoggerFactory.getLogger(AuthGroup.class);

  public AuthGroup(ArrayList<String> actions, ArrayList<Area> allowedAreas,
      Schedule schedule) {
    this.actions = actions;
    this.allowedAreas = allowedAreas;
    this.schedule = schedule;
  }

  // Validates a door access request against the three dimensions (when, where, what)
  // and collects all specific denial reasons to comply with the API specification.
  public ArrayList<String> checkPermissions(String action, Door door, LocalDateTime now, String role) {
    ArrayList<String> denialReasons = new ArrayList<>();

    // Checks the 'when' (time schedule)
    if (!schedule.isWorking(now)) {
      log.warn("[AUTH GROUP] Out of schedule for timestamp {}", now);
      denialReasons.add("Present date and/or time not in schedule");
    }

    // Checks the 'where' (destination space of the door)
    String destination = door.getToSpace();
    Visitor toFind = new AreaFinder(destination);
    for (Area a : allowedAreas) {
      a.acceptVisitor(toFind);
      if (toFind.getArea() != null) {
        break;
      }
    }

    if (toFind.getArea() == null) {
      log.warn("[AUTH GROUP] Invalid area: destination '{}' not allowed", destination);
      String groupSuffix = (role != null && !role.isEmpty()) ? " for group " + role : "";
      denialReasons.add("Not allowed destination " + destination + groupSuffix);
    }

    // Checks the 'what' (allowed action)
    if (!actions.contains(action)) {
      log.warn("[AUTH GROUP] Invalid action: '{}'", action);
      String groupSuffix = (role != null && !role.isEmpty()) ? " for group " + role : "";
      denialReasons.add("Not allowed action " + action + groupSuffix);
    }

    return denialReasons;
  }

  public boolean isAllowed(String action, String id, LocalDateTime now) {
    Door door = DirectoryDoors.getInstance().findDoorById(id);
    if (door != null) {
      return checkPermissions(action, door, now, null).isEmpty();
    }

    if (!actions.contains(action) || !schedule.isWorking(now)) {
      return false;
    }

    Visitor toFind = new AreaFinder(id);
    for (Area a : allowedAreas) {
      a.acceptVisitor(toFind);
      if (toFind.getArea() != null) {
        return true;
      }
    }

    return false;
  }
}

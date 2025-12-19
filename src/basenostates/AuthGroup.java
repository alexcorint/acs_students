package basenostates;

import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AUTHORIZATION GROUP CLASS.
 * Defines the type of employee, the allowed actions and
 * its schedule.
 */
public class AuthGroup {
  private final ArrayList<String> actions;
  private final ArrayList<Area> allowedAreas;
  private final Schedule schedule;
  private final Logger log = LoggerFactory.getLogger(AuthGroup.class);

  /**
   * Constructor for the AuthGroup class.
   * @param actions The list of allowed action strings (e.g., "open", "unlock").
   * @param allowedAreas The list of areas this group can access.
   * @param schedule The time schedule when access is permitted.
   */
  public AuthGroup(ArrayList<String> actions, ArrayList<Area> allowedAreas,
                   Schedule schedule) {
    this.actions = actions;
    this.allowedAreas = allowedAreas;
    this.schedule = schedule;
  }

  /**
   * Checks if the action is allowed for the given ID at the specific time.
   * @param action The action to perform (e.g., "lock").
   * @param id The ID of the door or area.
   * @param now The current timestamp.
   * @return true if the action is allowed, false otherwise.
   */
  public boolean isAllowed(String action, String id, LocalDateTime now) {
    // Checks the 'what'
    if (!actions.contains(action)) {
      log.warn("[AUTH GROUP] Invalid action");
      return false;
    }

    // Checks the 'where'
    Visitor toFind = new AreaFinder(id);
    for (Area a : allowedAreas) {
      a.acceptVisitor(toFind);
    }

    if (toFind.getArea() == null) {
      log.warn("[AUTH GROUP] Invalid area");
      return false;
    }

    // Checks the 'when'
    if (!schedule.isWorking(now)) {
      log.warn("[AUTH GROUP] Out of schedule");
      return false;
    }

    return true;
  }
}

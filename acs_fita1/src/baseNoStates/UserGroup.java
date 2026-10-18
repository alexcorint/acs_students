package baseNoStates;

import java.time.LocalDateTime;
import java.util.ArrayList;

// group of users (RBAC): the permisions are given to the group, not to each user.
// what = actions, where = areas, when = schedule
public class UserGroup {
  private final String name;
  private final ArrayList<String> actions;
  private final ArrayList<Area> areas;
  private Schedule schedule; // null = blank, never

  public UserGroup(String name, ArrayList<String> actions, ArrayList<Area> areas,
      Schedule schedule) {
    this.name = name;
    this.actions = actions;
    this.areas = areas;
    this.schedule = schedule;
  }

  public String getName() {
    return name;
  }

  // in case we want to change the schedule of the group
  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public boolean canDoAction(String action) {
    return actions.contains(action);
  }

  public boolean isInSchedule(LocalDateTime dateTime) {
    if (schedule == null) {
      return false;
    }
    return schedule.isInSchedule(dateTime);
  }

  // true if the space is inside one of the areas of the group
  public boolean canBeInSpace(Space space) {
    if (space == null) {
      return false;
    }
    for (Area area : areas) {
      if (area.findAreaById(space.getId()) != null) {
        return true;
      }
    }
    return false;
  }
}

package basenostates;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DirectoryUsers {
  private static final ArrayList<User> users = new ArrayList<>();
  private static final Logger log = LoggerFactory.getLogger(DirectoryUsers.class);
  private static final Map<String, AuthGroup> rolesMap = new HashMap<>();

  private static final String CONFIG_FILE = "src/resources/security_config.json";
  private static final String USERS_FILE = "src/resources/users.json";

  public static void makeUsers() {
    loadRoles();
    loadUsers();
  }

  private static void loadRoles() {
    try {
      String content = new String(Files.readAllBytes(Paths.get(CONFIG_FILE)));
      JSONObject root = new JSONObject(content);
      JSONObject jsonRoles = root.getJSONObject("roles");

      // Parse every role
      for (String roleName : jsonRoles.keySet()) {
        JSONObject jsonRole = jsonRoles.getJSONObject(roleName);

        // Parse schedule
        JSONObject jsonSchedule = jsonRole.getJSONObject("schedule");
        ArrayList<DayOfWeek> days = new ArrayList<>();
        JSONArray daysArray = jsonSchedule.getJSONArray("days");

        for (int i = 0; i < daysArray.length(); i++) {
          days.add(DayOfWeek.valueOf(daysArray.getString(i)));
        }

        Schedule schedule = new Schedule(
            LocalDate.parse(jsonSchedule.getString("startDate")),
            LocalTime.parse(jsonSchedule.getString("startTime")),
            LocalDate.parse(jsonSchedule.getString("endDate")),
            LocalTime.parse(jsonSchedule.getString("endTime")),
            days
        );

        // Parse allowed actions
        ArrayList<String> actions = new ArrayList<>();
        JSONArray jsonActions = jsonRole.getJSONArray("actions");

        if (!jsonActions.isEmpty() && jsonActions.getString(0).equalsIgnoreCase("ALL")) {
          actions.addAll(Actions.ALL_ACTIONS);
        } else {
          for (int i = 0; i < jsonActions.length(); i++) {
            actions.add(jsonActions.getString(i));
          }
        }

        // Parse areas
        ArrayList<Area> areas = new ArrayList<>();
        JSONArray jsonAreas = jsonRole.getJSONArray("allowed_areas");
        for (int i = 0; i < jsonAreas.length(); i++) {
          String areaId = jsonAreas.getString(i);
          areas.add(DirectoryAreas.findArea(areaId));
        }

        // Create auth level
        AuthGroup group = new AuthGroup(actions, areas,  schedule);
        rolesMap.put(roleName, group);
      }
      log.info("Roles loaded successfully.");

    } catch (IOException e) {
      log.error("Error reading security config file");
      log.error(e.getMessage());
    }
  }

  private static void loadUsers() {
    try {
      String content = new String(Files.readAllBytes(Paths.get(USERS_FILE)));
      JSONArray root = new JSONArray(content);

      for (int i = 0; i < root.length(); i++) {
        JSONObject jsonUser = root.getJSONObject(i);

        String name = jsonUser.getString("name");
        String credentials = jsonUser.getString("credential");
        String role = jsonUser.getString("role");
        User user = new User(name, credentials, role);

        if (rolesMap.containsKey(role)) {
          user.setAuthGroup(rolesMap.get(role));
        } else {
          log.warn("Role '{}' not found for user '{}'", role, name);
          user.setAuthGroup(new AuthGroup(new ArrayList<>(), new ArrayList<>(), new Schedule()));
        }

        users.add(user);
      }

      log.info("Users loaded successfully.");

    } catch (IOException e) {
      log.error("Error reading users file");
      log.error(e.getMessage());
    }
  }

  public static User findUserByCredential(String credential) {
    for (User user : users) {
      if (user.getCredential().equals(credential)) {
        return user;
      }
    }
    System.out.println("user with credential " + credential + " not found");
    return null; // otherwise we get a Java error
  }

}

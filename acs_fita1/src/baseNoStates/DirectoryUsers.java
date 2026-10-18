package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

// creates the groups and the users. It uses DirectoryAreas so makeAreas() goes before (see Main)
public final class DirectoryUsers {
  private static final ArrayList<User> users = new ArrayList<>();

  public static void makeUsers() {
    ArrayList<DayOfWeek> monToFri = new ArrayList<>(Arrays.asList(DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY));
    ArrayList<DayOfWeek> monToSat = new ArrayList<>(monToFri);
    monToSat.add(DayOfWeek.SATURDAY);
    ArrayList<DayOfWeek> allDays = new ArrayList<>(Arrays.asList(DayOfWeek.values()));
    ArrayList<String> allActions = new ArrayList<>(Arrays.asList(Actions.OPEN, Actions.CLOSE,
        Actions.LOCK, Actions.UNLOCK, Actions.UNLOCK_SHORTLY));

    // users without any privilege, just to keep temporally users instead of deleting them,
    // this is to withdraw all permissions but still to keep user data to give back
    // permissions later
    UserGroup blank = new UserGroup("blank", new ArrayList<>(), new ArrayList<>(), null);
    users.add(new User("Bernat", "12345", blank));
    users.add(new User("Blai", "77532", blank));

    // employees :
    // Sep. 1 this year to Mar. 1 next year
    // week days 9-17h
    // just shortly unlock
    // ground floor, floor1, exterior, stairs (this, for all), that is, everywhere but the parking
    Schedule employeesSchedule = new Schedule(LocalDate.of(2026, 9, 1), LocalDate.of(2027, 3, 1),
        monToFri, LocalTime.of(9, 0), LocalTime.of(17, 0));
    ArrayList<String> employeesActions = new ArrayList<>(Arrays.asList(Actions.OPEN,
        Actions.CLOSE, Actions.UNLOCK_SHORTLY));
    ArrayList<Area> employeesAreas = new ArrayList<>(Arrays.asList(
        DirectoryAreas.findAreaById("ground_floor"), DirectoryAreas.findAreaById("floor1"),
        DirectoryAreas.findAreaById("exterior"), DirectoryAreas.findAreaById("stairs")));
    UserGroup employees = new UserGroup("employees", employeesActions, employeesAreas,
        employeesSchedule);
    users.add(new User("Ernest", "74984", employees));
    users.add(new User("Eulalia", "43295", employees));

    // managers :
    // Sep. 1 this year to Mar. 1 next year
    // week days + saturday, 8-20h
    // all actions
    // all spaces
    Schedule managersSchedule = new Schedule(LocalDate.of(2026, 9, 1), LocalDate.of(2027, 3, 1),
        monToSat, LocalTime.of(8, 0), LocalTime.of(20, 0));
    UserGroup managers = new UserGroup("managers", allActions,
        new ArrayList<>(Arrays.asList(DirectoryAreas.findAreaById("building"))),
        managersSchedule);
    users.add(new User("Manel", "95783", managers));
    users.add(new User("Marta", "05827", managers));

    // admin :
    // always=Jan. 1 this year to 2100
    // all days of the week
    // all actions
    // all spaces
    Schedule adminSchedule = new Schedule(LocalDate.of(2026, 1, 1), LocalDate.of(2100, 1, 1),
        allDays, LocalTime.MIN, LocalTime.MAX); // all the day
    UserGroup admin = new UserGroup("admin", allActions,
        new ArrayList<>(Arrays.asList(DirectoryAreas.findAreaById("building"))),
        adminSchedule);
    users.add(new User("Ana", "11343", admin));
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

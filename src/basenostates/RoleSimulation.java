package basenostates;

import java.time.LocalDateTime;
import java.time.Month;

public class RoleSimulation {

  public static void main(String[] args) {
    System.out.println("Initializing system...");
    DirectoryDoors.getInstance();
    DirectoryAreas.getInstance();
    DirectoryUsers.getInstance();

    System.out.println("Starting Role Simulation...");
    System.out.println("--------------------------------------------------");

    // Test Cases
    testUser("Bernat", "open", "D3", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), false); // Visitor, no access

    // Ernest (Employee) - Mon-Fri 09-17, everywhere but parking
    testUser("Ernest", "open", "D3", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), true); // Thu 10:00, Hall (Ground
                                                                                             // Floor) -> OK
    testUser("Ernest", "open", "D3", LocalDateTime.of(2026, Month.OCTOBER, 1, 20, 0), false); // Thu 20:00 -> Fail
                                                                                              // (Time)
    testUser("Ernest", "open", "D3", LocalDateTime.of(2026, Month.OCTOBER, 3, 10, 0), false); // Sat 10:00 -> Fail (Day)
    testUser("Ernest", "open", "D1", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), false); // Parking (Destination
                                                                                              // not allowed) -> Fail
    testUser("Ernest", "unlock", "D3", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), false); // Unlock action not
                                                                                                // allowed -> Fail
                                                                                                // (Action)
    testUser("Ernest", "unlock_shortly", "D3", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), true); // Unlock_shortly
                                                                                                       // allowed -> OK
    testUser("Ernest", "open", "D9", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), true); // IT (Floor 1) -> OK

    // Manel (Manager) - Mon-Sat 08-20, all spaces, all actions
    testUser("Manel", "open", "D1", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), true); // Parking -> OK
    testUser("Manel", "open", "D1", LocalDateTime.of(2026, Month.OCTOBER, 4, 10, 0), false); // Sun 10:00 -> Fail (Day)
    testUser("Manel", "lock", "D1", LocalDateTime.of(2026, Month.OCTOBER, 1, 10, 0), true); // Lock -> OK

    // Ana (Admin) - 24/7
    testUser("Ana", "open", "D1", LocalDateTime.of(2026, Month.OCTOBER, 4, 3, 0), true); // Sun 03:00 -> OK

    System.out.println("--------------------------------------------------");
    System.out.println("Simulation Complete.");
  }

  private static void testUser(String userName, String action, String resourceId, LocalDateTime time,
      boolean expectedResult) {
    User user = findUserByName(userName);
    if (user == null) {
      System.out.println("ERROR: User " + userName + " not found!");
      return;
    }

    AuthGroup authGroup = user.getAuthGroup();
    boolean result = authGroup.isAllowed(action, resourceId, time);

    String status = (result == expectedResult) ? "PASS" : "FAIL";
    System.out.printf(
        "[%s] User: %-10s | Action: %-10s | Resource: %-5s | Time: %-20s | Expected: %-5s | Actual: %-5s%n",
        status, userName, action, resourceId, time, expectedResult, result);

    if (!status.equals("PASS")) {
      System.out.println("    -> Potential Error or Configuration Issue.");
    }
  }

  private static User findUserByName(String name) {
    DirectoryUsers dir = DirectoryUsers.getInstance();
    return switch (name) {
      case "Bernat" -> dir.findUserByCredential("12345");
      case "Blai" -> dir.findUserByCredential("77532");
      case "Ernest" -> dir.findUserByCredential("74984");
      case "Eulalia" -> dir.findUserByCredential("43295");
      case "Manel" -> dir.findUserByCredential("95783");
      case "Marta" -> dir.findUserByCredential("05827");
      case "Ana" -> dir.findUserByCredential("11343");
      default -> null;
    };
  }
}
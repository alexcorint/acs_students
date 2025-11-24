package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DirectoryDoors {
  private static ArrayList<Door> allDoors;
  private static final Logger log =  LoggerFactory.getLogger(DirectoryDoors.class);

  public static void makeDoors() {
    // basement
    Door d1 = new Door("D1", "Parking"); // exterior, parking
    Door d2 = new Door("D2", "Parking"); // stairs, parking

    // ground floor
    Door d3 = new Door("D3", "Hall"); // exterior, hall
    Door d4 = new Door("D4", "Hall"); // stairs, hall
    Door d5 = new Door("D5", "Room1"); // hall, room1
    Door d6 = new Door("D6", "Room1"); // hall, room2

    // first floor
    Door d7 = new Door("D7", "Corridor"); // stairs, corridor
    Door d8 = new Door("D8", "Room3"); // corridor, room3
    Door d9 = new Door("D9", "IT"); // corridor, IT

    allDoors = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9));
    log.info("[DIRECTORY DOORS] Success: Initialized {} doors",  allDoors.size());
  }

  public static Door findDoorById(String id) {
    for (Door door : allDoors) {
      if (door.getId().equals(id)) {
        return door;
      }
    }
    log.debug("Door '{}' not found", id);
    return null; // otherwise we get a Java error
  }

  // this is needed by RequestRefresh
  public static ArrayList<Door> getAllDoors() {
    return allDoors;
  }

}

package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// DirectoryDoors implements the Singleton design pattern to provide a single,
// globally accessible repository of all Door objects in the access control system.
// A single instance ensures consistent door states across all request threads.
public final class DirectoryDoors {
  private static DirectoryDoors instance = null;
  private final ArrayList<Door> allDoors;
  private static final Logger log = LoggerFactory.getLogger(DirectoryDoors.class);

  private DirectoryDoors() {
    this.allDoors = new ArrayList<>();
    initDoors();
  }

  public static DirectoryDoors getInstance() {
    if (instance == null) {
      instance = new DirectoryDoors();
    }
    return instance;
  }

  private void initDoors() {
    // basement
    Door d1 = new Door("D1", "exterior", "parking");
    Door d2 = new Door("D2", "stairs", "parking");

    // ground floor
    Door d3 = new Door("D3", "exterior", "hall");
    Door d4 = new Door("D4", "stairs", "hall");
    Door d5 = new Door("D5", "hall", "room1");
    Door d6 = new Door("D6", "hall", "room2");

    // first floor
    Door d7 = new Door("D7", "stairs", "corridor");
    Door d8 = new Door("D8", "corridor", "room3");
    Door d9 = new Door("D9", "corridor", "IT");

    allDoors.addAll(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9));
    log.info("[DIRECTORY DOORS] Success: Initialized {} doors", allDoors.size());
  }

  public Door findDoorById(String id) {
    for (Door door : allDoors) {
      if (door.getId().equals(id)) {
        return door;
      }
    }
    log.debug("Door '{}' not found", id);
    return null;
  }

  public ArrayList<Door> getAllDoors() {
    return allDoors;
  }
}

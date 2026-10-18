package baseNoStates;

import java.util.ArrayList;
import java.util.Arrays;

// makes the tree of the building (the last hierarchy of figure 8) and the doors.
// It replaces DirectoryDoors, now to make a door we need his two spaces
public final class DirectoryAreas {
  private static Area rootArea;
  private static ArrayList<Door> allDoors;

  public static void makeAreas() {
    Partition building = new Partition("building", null); // root

    // basement
    Partition basement = new Partition("basement", building);
    Space parking = new Space("parking", basement);

    // ground floor
    Partition groundFloor = new Partition("ground_floor", building);
    Space hall = new Space("hall", groundFloor);
    Space room1 = new Space("room1", groundFloor);
    Space room2 = new Space("room2", groundFloor);

    // first floor
    Partition floor1 = new Partition("floor1", building);
    Space corridor = new Space("corridor", floor1);
    Space room3 = new Space("room3", floor1);
    Space it = new Space("IT", floor1);

    // stairs and exterior are not in any floor
    Space stairs = new Space("stairs", building);
    Space exterior = new Space("exterior", building);

    rootArea = building;

    // id, from (where is the reader), to
    Door d1 = new Door("D1", exterior, parking);
    Door d2 = new Door("D2", stairs, parking);

    Door d3 = new Door("D3", exterior, hall);
    Door d4 = new Door("D4", stairs, hall);
    Door d5 = new Door("D5", hall, room1);
    Door d6 = new Door("D6", hall, room2);

    Door d7 = new Door("D7", stairs, corridor);
    Door d8 = new Door("D8", corridor, room3);
    Door d9 = new Door("D9", corridor, it);

    allDoors = new ArrayList<>(Arrays.asList(d1, d2, d3, d4, d5, d6, d7, d8, d9));
  }

  public static Area findAreaById(String id) {
    Area area = rootArea.findAreaById(id);
    if (area == null) {
      System.out.println("area with id " + id + " not found");
    }
    return area;
  }

  public static Door findDoorById(String id) {
    for (Door door : allDoors) {
      if (door.getId().equals(id)) {
        return door;
      }
    }
    System.out.println("door with id " + id + " not found");
    return null; // otherwise we get a Java error
  }

  // this is needed by RequestRefresh
  public static ArrayList<Door> getAllDoors() {
    // System.out.println(allDoors);
    return allDoors;
  }
}

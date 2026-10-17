package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// DirectoryAreas implements the Singleton design pattern. It manages the whole
// area hierarchy (Partitions and Spaces) of the building and allows finding areas
// through the Visitor pattern. Having a single instance avoids duplicate area trees.
public class DirectoryAreas {
  private static DirectoryAreas instance = null;
  private final ArrayList<Area> areas;
  private static final Logger log = LoggerFactory.getLogger(DirectoryAreas.class);

  private DirectoryAreas() {
    this.areas = new ArrayList<>();
    initAreas();
  }

  public static DirectoryAreas getInstance() {
    if (instance == null) {
      instance = new DirectoryAreas();
    }
    return instance;
  }

  private void initAreas() {
    ArrayList<Door> doors = DirectoryDoors.getInstance().getAllDoors();

    // Main spaces
    Space parking = new Space("parking",
        new ArrayList<>(Arrays.asList(doors.get(0), doors.get(1))));
    Space hall = new Space("hall",
        new ArrayList<>(Arrays.asList(doors.get(2), doors.get(3))));
    Space stairs = new Space("stairs",
        new ArrayList<>());
    Space corridor = new Space("corridor",
        new ArrayList<>(Collections.singletonList(doors.get(6))));
    Space exterior = new Space("exterior",
        new ArrayList<>());

    // Room spaces
    Space room1 = new Space("room1",
        new ArrayList<>(Collections.singletonList(doors.get(4))));
    Space room2 = new Space("room2",
        new ArrayList<>(Collections.singletonList(doors.get(5))));
    Space room3 = new Space("room3",
        new ArrayList<>(Collections.singletonList(doors.get(7))));
    Space it = new Space("it",
        new ArrayList<>(Collections.singletonList(doors.get(8))));

    // Main partitions
    Partition basement = new Partition("basement",
        new ArrayList<>(Collections.singletonList(parking)));
    Partition groundFloor = new Partition("ground floor",
        new ArrayList<>(Arrays.asList(hall, room1, room2)));
    Partition floor1 = new Partition("floor 1",
        new ArrayList<>(Arrays.asList(corridor, it, room3)));

    // Main building
    areas.add(new Partition("building",
        new ArrayList<>(Arrays.asList(basement, groundFloor, floor1, stairs, exterior))));

    log.info("[DIRECTORY AREAS] Success: Building loaded");
  }

  public Area findArea(String id) {
    if (id.equalsIgnoreCase("Root")) {
      return areas.getFirst();
    }

    Visitor finder = new AreaFinder(id);
    for (Area area : areas) {
      area.acceptVisitor(finder);
      if (finder.getArea() != null) {
        break;
      }
    }
    return finder.getArea();
  }

  public ArrayList<Area> getAreas() {
    return areas;
  }
}
package basenostates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DIRECTORY AREAS CLASS.
 * Manages the hierarchy of areas (Spaces and Partitions) and
 * provides access to them via ID.
 */
public class DirectoryAreas {
  private static ArrayList<Area> areas;
  private static final Logger log = LoggerFactory.getLogger(DirectoryAreas.class);

  /**
   * Creates and organizes the hierarchy of spaces and partitions.
   * @param doors The list of door instances to be distributed in the areas.
   */
  public static void defineAreas(ArrayList<Door> doors) {
    // Main spaces
    Space parking = new Space("parking",
        new ArrayList<>(Arrays.asList(doors.get(0), doors.get(1))));
    Space hall = new Space("hall",
        new ArrayList<>(Arrays.asList(doors.get(2), doors.get(3), doors.get(4), doors.get(5))));
    Space stairs = new Space("stairs",
        new ArrayList<>(Arrays.asList(doors.get(1), doors.get(3), doors.get(6))));
    Space corridor = new Space("corridor",
        new ArrayList<>(Arrays.asList(doors.get(6), doors.get(7), doors.get(8))));
    Space exterior = new Space("exterior",
        new ArrayList<>(Arrays.asList(doors.get(0), doors.get(2))));

    // Singletons
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
    areas = new ArrayList<>(Collections.singletonList(
        new Partition("building",
            new ArrayList<>(Arrays.asList(basement, groundFloor, floor1, stairs, exterior)))));

    log.info("[DIRECTORY AREAS] Success: Building loaded");
  }

  /**
   * Searches for a specific area by its ID using the Visitor pattern.
   * @param id The identification string of the area to find.
   * @return The Area object if found, or the root area otherwise.
   */
  public static Area findArea(String id) {
    if (id.equals("Root") || id.equals("ROOT")) {
      return areas.getFirst();
    }

    Visitor finder = new AreaFinder(id);

    for (Area area : areas) {
      area.acceptVisitor(finder);
    }
    return finder.getArea();
  }

  /**
   * Gets the root list of areas.
   * @return The ArrayList containing the root areas.
   */
  public static ArrayList<Area> getAreas() {
    return areas;
  }
}
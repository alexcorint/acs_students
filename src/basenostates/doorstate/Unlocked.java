package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Concrete state representing an unlocked door. In this state, users can freely
// open and close the door. An authorized lock action transitions the door back to
// Locked if and only if the door is physically closed.
public class Unlocked extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Unlocked.class);

  public Unlocked(Door door) {
    super(door);
  }

  @Override
  public String toString() {
    return "unlocked";
  }

  @Override
  public void unlock() {
    log.warn("[UNLOCKED] Redundant action: The door is already unlocked");
  }

  @Override
  public void unlockShortly() {
    log.warn("[UNLOCKED] Redundant action: The door is already unlocked");
  }

  @Override
  public void open() {
    log.debug("[UNLOCKED] Trying to open the door...");
    if (!door.isClosed()) {
      log.warn("[UNLOCKED] The door is already open");
      return;
    }
    door.setClosed(false);
  }

  @Override
  public void lock() {
    log.debug("[UNLOCKED] Trying to lock the door...");
    if (!door.isClosed()) {
      log.warn("[UNLOCKED] Cannot lock an open door. Door must be closed first");
      return;
    }
    door.setState(new Locked(door));
  }

  @Override
  public void close() {
    log.debug("[UNLOCKED] Trying to close the door...");
    if (door.isClosed()) {
      log.warn("[UNLOCKED] The door is already closed");
      return;
    }
    door.setClosed(true);
  }
}

package basenostates.doorstate;

import basenostates.Door;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Concrete state representing an alarm state: a door that should be closed remains open
// past the maximum allowed duration (10 seconds) of the UnlockedShortly state.
// All regular actions are rejected until the door is physically closed, returning it to Locked.
public class Propped extends DoorState {
  private static final Logger log = LoggerFactory.getLogger(Propped.class);

  public Propped(Door door) {
    super(door);
  }

  @Override
  public String toString() {
    return "propped";
  }

  @Override
  public void unlock() {
    log.warn("[PROPPED] Rejected action: Cannot unlock a Propped door in alarm");
  }

  @Override
  public void unlockShortly() {
    log.warn("[PROPPED] Rejected action: Cannot unlock shortly a Propped door");
  }

  @Override
  public void open() {
    log.warn("[PROPPED] Rejected action: A Propped door is already open");
  }

  @Override
  public void lock() {
    log.warn("[PROPPED] Rejected action: Cannot lock a door while propped open");
  }

  @Override
  public void close() {
    log.info("[PROPPED] Door closed by reader request. Clearing alarm, door is now locked");
    door.setClosed(true);
    door.setState(new Locked(door));
  }
}

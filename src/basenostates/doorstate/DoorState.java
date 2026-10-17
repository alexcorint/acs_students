package basenostates.doorstate;

import basenostates.Door;

// Base class of the State design pattern applied to physical doors.
// Instead of complex if-else conditionals in Door, each possible state (Unlocked,
// Locked, UnlockedShortly, Propped) encapsulates its own behavior for open, close,
// lock, unlock, and unlockShortly actions, making state transitions explicit and
// allowing new states (e.g. Do Not Disturb) to be added without modifying Door.
public abstract class DoorState {
  protected final Door door;

  public DoorState(Door door) {
    this.door = door;
  }

  public abstract void unlock();

  public abstract void open();

  public abstract void lock();

  public abstract void close();

  public void unlockShortly() {}
}

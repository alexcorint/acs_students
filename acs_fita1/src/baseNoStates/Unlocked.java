package baseNoStates;

// unlocked: the door can be opened and closed, and locked only if is closed
public class Unlocked extends DoorState {

  public Unlocked(Door door) {
    super(door);
    name = States.UNLOCKED;
  }

  @Override
  public void open() {
    if (door.isClosed()) {
      door.setClosed(false);
    } else {
      System.out.println("Can't open door " + door.getId() + " because it's already open");
    }
  }

  @Override
  public void close() {
    if (door.isClosed()) {
      System.out.println("Can't close door " + door.getId() + " because it's already closed");
    } else {
      door.setClosed(true);
    }
  }

  @Override
  public void lock() {
    // if is open we cant lock it
    if (door.isClosed()) {
      door.setState(new Locked(door));
    } else {
      System.out.println("Can't lock door " + door.getId() + " because it's open");
    }
  }

  @Override
  public void unlock() {
    System.out.println("Door " + door.getId() + " is already unlocked");
  }

  @Override
  public void unlockShortly() {
    // ja esta desbloquejada, no fem res
    System.out.println("Door " + door.getId() + " is unlocked, no need to unlock it shortly");
  }
}

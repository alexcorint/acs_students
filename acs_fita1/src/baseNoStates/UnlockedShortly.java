package baseNoStates;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Observable;
import java.util.Observer;

// unlocked only 10 seconds, after that if its closed goes to locked and if its open to propped.
// We can't do Thread.sleep because all the server would stop, thats why this state
// observes the Clock that tells it the time every second (Observer pattern)
public class UnlockedShortly extends DoorState implements Observer {
  private static final int SECONDS_UNLOCKED = 10;
  private final LocalDateTime startTime; // real time, not the date of the simulator

  public UnlockedShortly(Door door) {
    super(door);
    name = States.UNLOCKED_SHORTLY;
    startTime = LocalDateTime.now();
    Clock.getInstance().addObserver(this);
  }

  // the clock calls this every second
  @Override
  public void update(Observable o, Object arg) {
    LocalDateTime now = (LocalDateTime) arg;
    long seconds = Duration.between(startTime, now).getSeconds();
    // System.out.println("door " + door.getId() + " unlocked shortly, " + seconds + " s");
    if (seconds >= SECONDS_UNLOCKED) {
      // stop observing, if not we keep reciving updates for ever
      Clock.getInstance().deleteObserver(this);
      if (door.isClosed()) {
        door.setState(new Locked(door));
        System.out.println("Door " + door.getId() + " locked again after "
            + SECONDS_UNLOCKED + " seconds");
      } else {
        door.setState(new Propped(door));
        System.out.println("Door " + door.getId() + " is propped! it's open after "
            + SECONDS_UNLOCKED + " seconds");
      }
    }
  }

  // open and close like in unlocked
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
    // it locks alone when the time finish
    System.out.println("Can't lock door " + door.getId() + " now, it is unlocked shortly");
  }

  @Override
  public void unlock() {
    System.out.println("Can't unlock door " + door.getId() + ", it is unlocked shortly");
  }

  @Override
  public void unlockShortly() {
    System.out.println("Door " + door.getId() + " is already unlocked shortly");
  }
}

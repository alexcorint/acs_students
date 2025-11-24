package basenostates.doorstate;

import basenostates.Door;

/**
 * DOOR STATE INTERFACE.
 * The interface that takes control over the state of a
 * Door instance
 */
public abstract class DoorState {
  protected final Door door;

  /**
   * Constructor for the DoorState interface.
   *
   * @param door the Door instance to be controlled.
   */
  public DoorState(Door door) {
    this.door = door;
  }

  /**
   * Method call to Unlock door action.
   */
  public abstract void unlock();

  /**
   * Method call to Open door action.
   */
  public abstract void open();

  /**
   * Method call to Lock door action.
   */
  public abstract void lock();

  /**
   * Method call to Close door action.
   */
  public abstract void close();

  /**
   * Method call to Unlock Shortly door action.
   */
  public void unlockShortly() {}
}

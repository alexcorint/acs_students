package basenostates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Schedule {
  private LocalDate startDate;
  private LocalTime startTime;
  private LocalDate endDate;
  private LocalTime endTime;
  private ArrayList<DayOfWeek> daysOfWeek;

  /**
   * Empty constructor if blank schedules needed
   * for no access.
   */
  public Schedule() {}

  /**
   * Constructor for the Schedule class.
   * @param startDate The starting date of the permission validity.
   * @param startTime The starting hour of the daily access.
   * @param endDate The ending date of the permission validity.
   * @param endTime The ending hour of the daily access.
   * @param daysOfWeek The list of allowed days of the week.
   */
  public Schedule(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                  LocalTime endTime, ArrayList<DayOfWeek> daysOfWeek) {
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
    this.daysOfWeek = daysOfWeek;
  }

  /**
   * Checks if the provided timestamp is within the allowed schedule.
   * @param dateTime The LocalDateTime to check.
   * @return true if access is allowed at this moment.
   */
  public boolean isWorking(LocalDateTime dateTime) {
    if (startDate == null || endDate == null) {
      return false;
    }

    LocalDate date = dateTime.toLocalDate();
    LocalTime time = dateTime.toLocalTime();

    if (date.isBefore(startDate) || date.isAfter(endDate)) {
      return false;
    }

    if (time.isBefore(startTime) || time.isAfter(endTime)) {
      return false;
    }

    if (!daysOfWeek.contains(dateTime.getDayOfWeek())) {
      return false;
    }

    return true;
  }
}

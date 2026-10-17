package basenostates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

// Represents the temporal dimension of access rights.
// Encapsulates valid date range (startDate to endDate), daily time window (startTime to endTime),
// and allowed days of the week using the modern java.util.time API.
public class Schedule {
  private LocalDate startDate;
  private LocalTime startTime;
  private LocalDate endDate;
  private LocalTime endTime;
  private ArrayList<DayOfWeek> daysOfWeek;

  public Schedule() {}

  public Schedule(LocalDate startDate, LocalTime startTime, LocalDate endDate,
                  LocalTime endTime, ArrayList<DayOfWeek> daysOfWeek) {
    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
    this.daysOfWeek = daysOfWeek;
  }

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

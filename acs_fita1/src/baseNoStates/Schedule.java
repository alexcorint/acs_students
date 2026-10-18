package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

// schedule of a group: between two dates, some days of the week and between two hours
public class Schedule {
  private final LocalDate fromDate;
  private final LocalDate toDate;
  private final ArrayList<DayOfWeek> days;
  private final LocalTime fromHour;
  private final LocalTime toHour;

  public Schedule(LocalDate fromDate, LocalDate toDate, ArrayList<DayOfWeek> days,
                  LocalTime fromHour, LocalTime toHour) {
    this.fromDate = fromDate;
    this.toDate = toDate;
    this.days = days;
    this.fromHour = fromHour;
    this.toHour = toHour;
  }

  // with !isBefore and !isAfter the limits are included (at 9:00 its ok)
  public boolean isInSchedule(LocalDateTime dateTime) {
    LocalDate date = dateTime.toLocalDate();
    LocalTime time = dateTime.toLocalTime();
    boolean dateOk = !date.isBefore(fromDate) && !date.isAfter(toDate);
    boolean dayOk = days.contains(dateTime.getDayOfWeek());
    boolean hourOk = !time.isBefore(fromHour) && !time.isAfter(toHour);
    return dateOk && dayOk && hourOk;
  }
}

package depot;

import java.time.LocalDateTime;

public interface Schedulable {
	
	boolean isAvailable(LocalDateTime start, LocalDateTime end);
	void setSchedule(WorkSchedule newWorkSchedule);
}

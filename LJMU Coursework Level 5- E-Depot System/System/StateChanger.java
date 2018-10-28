package system;

import depot.Depot;
import depot.Status;
import depot.WorkSchedule;

// This is not done following OO principles as it is a hack to represent the passing of time.
public class StateChanger implements Runnable {

	private final System system;

	public StateChanger(System system) {
		this.system = system;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(1000 * 60 * 3);
				for (Depot depot : system.getDepots()) {
					for (WorkSchedule workSchedule : depot.getWorkSchedules()) {
						if (workSchedule.getState() == Status.PENDING) {
							workSchedule.setState(Status.ACTIVE);
						} else if (workSchedule.getState() == Status.ACTIVE) {
							workSchedule.setState(Status.ARCHIVE);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

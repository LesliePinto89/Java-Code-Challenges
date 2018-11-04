package depot;

public class Truck  extends Vehicle {
	private int cargoCapacity;

	public Truck(int cargoCapacity, String make, String model, int weight, String regNo){
		super(make, model, weight, regNo);
		this.cargoCapacity = cargoCapacity;
	}
	public int getCargoCapacity() {
		return cargoCapacity;
	}

	public void setCargoCapacity(int cargoCapacity) {
		this.cargoCapacity = cargoCapacity;
	}

	@Override
	public String toString() {
		return super.toString() + " Truck [cargoCapacity=" + cargoCapacity + "]";
	}
	
	
}

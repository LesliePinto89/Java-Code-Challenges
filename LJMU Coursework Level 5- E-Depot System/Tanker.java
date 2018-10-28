package depot;

public class Tanker extends Vehicle {
	private int liquidCapacity;
	private String liquidType;
	
	
	public Tanker(int liquidCapacity, String liquidType, String make, String model, int weight, String regNo){
		super(make, model, weight, regNo);
		this.liquidCapacity = liquidCapacity;
		this.liquidType = liquidType;
	}
	public int getLiquidCapacity() {
		return liquidCapacity;
	}
	
	public void setLiquidCapacity(int liquidCapacity) {
		this.liquidCapacity = liquidCapacity;
	}
	public String getLiquidType() {
		return liquidType;
	}
	public void setLiquidType(String liquidType) {
		this.liquidType = liquidType;
	}
	@Override
	public String toString() {
		return super.toString() + " Tanker [liquidCapacity=" + liquidCapacity + ", liquidType=" + liquidType + "]";
	}
	
	
}

package edu.brandeis.cs.cs131.pa3.submission;

import edu.brandeis.cs.cs131.pa3.tunnel.Car;
import edu.brandeis.cs.cs131.pa3.tunnel.Direction;
import edu.brandeis.cs.cs131.pa3.tunnel.Tunnel;
import edu.brandeis.cs.cs131.pa3.tunnel.Vehicle;

/**
 * The BasicTunnel enforces a basic admittance policy.
 * Vehicles are only allowed to enter if they are the right type of vehicle and are going the correct direction.
 * Additionally, vehicles cannot enter if the tunnel is at capacity.
 * It extends the Tunnel class.
 * @author jessebecklevisohn
 *
 */
public class BasicTunnel extends Tunnel {
	
	
	private int numCars = 0;
	private int numSleds = 0;
	private boolean isCar = false;
	private String direction = "NORTH";
	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	/** 
	 * This method checks whether the vehicle can enter and returns true if it can and false otherwise. 
	 * The method also updates the tunnel's fields with information about the vehicle that entered the tunnel
	 * if the vehicle can enter.
	 * @return boolean: true if the vehicle can enter, else false.
	 */
	protected boolean tryToEnterInner(Vehicle vehicle) {
		// Get the vehicles type
		boolean type = vehicle instanceof Car;
		
		if (type && this.numCars >= CAR_CAPACITY) {
			return false;
		}
		if (!type && this.numSleds >= SLED_CAPACITY) {
			return false;
		}
		
		// Get the vehicles direction
		String vehicleDirection = vehicle.getDirection().toString();
		
		
		// If there is capacity and the vehicle is the tunnel is empty, 
		// we know that the vehicle can enter so we skip this section. 
		if ((this.numCars + this.numSleds) != 0) {
			// Compare the vehicles type to the type of the other vehicles in the tunnel
			if (type != this.isCar) {
				return false;
			}
			
			// Compare the vehicles direction to the direction of the other vehicles in the tunnel
			if (!this.direction.equals(vehicleDirection)) {
				return false;
			}
		}
		
		// If the vehicle can enter, update the tunnels information
		this.isCar = type;
		
		if (this.isCar) {
			this.numCars ++;
		} else {
			this.numSleds ++;
		}
		
		this.direction = vehicleDirection;
		
		return true;
	}

	@Override
	/**
	 * Provides the implementation for the vehicle exiting the tunnel.
	 * It decrements the amount of vehicles in the tunnel.
	 * The method does not have to account for the situation where it is the last vehicle to leave a tunnel
	 * because the enterTunnelInner method checks whether it is entering an empty tunnel.
	 */
	protected void exitTunnelInner(Vehicle vehicle) {
		if (this.isCar) {
			this.numCars --;
		} else {
			this.numSleds --;
		}
	}

}

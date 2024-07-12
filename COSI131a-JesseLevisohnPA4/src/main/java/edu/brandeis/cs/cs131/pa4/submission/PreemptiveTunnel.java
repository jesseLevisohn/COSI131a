package edu.brandeis.cs.cs131.pa4.submission;

import java.util.HashSet;

import edu.brandeis.cs.cs131.pa4.tunnel.Ambulance;
import edu.brandeis.cs.cs131.pa4.tunnel.Car;
import edu.brandeis.cs.cs131.pa4.tunnel.Sled;
import edu.brandeis.cs.cs131.pa4.tunnel.Tunnel;

/**
 * The class for the Preemptive Tunnel, extending Tunnel.
 * 
 * @author jessebecklevisohn
 * jlevisohn@brandeis.edu
 */
public class PreemptiveTunnel extends Tunnel {
	private int numCars = 0;
	private int numSleds = 0;
	private int numAmbs = 0;
	private boolean isCar = false;
	private String direction = "NORTH";
	public HashSet<Vehicle> vehicles = new HashSet<Vehicle>();
	
	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	public PreemptiveTunnel(String name) {
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
		
		// If the vehicle is an ambulance call the enterAmbulance Method
		if (vehicle instanceof Ambulance) {
			return enterAmbulance();
		} 
		
		
		
		boolean type = vehicle instanceof Car;
				
		if (type && this.numCars >= CAR_CAPACITY) {
			return false;
		}
		if (!type && this.numSleds >= SLED_CAPACITY) {
			return false;
		}
				
		// Get the vehicles direction
		String vehicleDirection = vehicle.getDirection().toString();
				
				
		// If there is capacity and the the tunnel is empty, 
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
		this.vehicles.add(vehicle);
		return true;
	}
	
	/**
	 * This method has implements the ambulance trying to enter a tunnel.
	 * If there are no ambulances, the ambulance will enter a tunnel 
	 * and interrupt all of the other vehicles already in the tunnel
	 * @return boolean, whether the ambulance was able to enter the tunnel or not
	 */
	public boolean enterAmbulance() {
		if (this.numAmbs != 0) {
			return false;
		} else {
			// Update the number of ambulances in the tunnel
			this.numAmbs ++;
			
			// Interrupt all of the vehicles currently in the tunnel
			for (Vehicle v : vehicles) {
				v.interrupt();
			}
			return true;
		}
	}
	
	@Override
	/**
	 * Provides the implementation for the vehicle exiting the tunnel.
	 * It decrements the amount of vehicles in the tunnel.
	 * The method does not have to account for the situation where it is the last vehicle to leave a tunnel
	 * because the enterTunnelInner method checks whether it is entering an empty tunnel.
	 */
	protected void exitTunnelInner(Vehicle vehicle) {
		if (vehicle instanceof Car) {
			this.numCars --;
		} else if (vehicle instanceof Sled) {
			this.numSleds --;
		} else {
			this.numAmbs --;
		}
		vehicles.remove(vehicle);
	}
	
}

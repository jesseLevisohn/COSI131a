package edu.brandeis.cs.cs131.pa4.submission;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import edu.brandeis.cs.cs131.pa4.scheduler.Scheduler;
import edu.brandeis.cs.cs131.pa4.tunnel.Tunnel;


/**
 * The preemptive priority scheduler assigns vehicles to tunnels based on their priority and supports 
 * preemption with ambulances.
 * It extends the Scheduler class.
 * 
 * @author jessebecklevisohn
 * jlevisohn@brandeis.edu
 */
public class PreemptivePriorityScheduler extends Scheduler {
	
	private HashSet<Vehicle> entrySet = new HashSet<Vehicle>();
	public HashMap<Vehicle, Tunnel> tMap = new HashMap<Vehicle, Tunnel>();
	
	final ReentrantLock lock = new ReentrantLock();
	final Condition cv = lock.newCondition();
	
	
	public PreemptivePriorityScheduler(String name, Collection<Tunnel> tunnels) {
		super(name, tunnels);
	}

	@Override
	/**
	 * This method attempts to admit a vehicle to a tunnel. 
	 * The vehicle will be admitted if the vehicle has the highest priority and if there is an open tunnel.
	 * This method is synchronized to ensure mutual exclusion
	 * @param vehicle, the vehicle to be admitted
	 * @return Tunnel: the tunnel that the vehicle entered if it entered a tunnel, otherwise null.
	 */
	public synchronized Tunnel admit(Vehicle vehicle) {
		try {
			while (true) {
				
				// First we add the vehicle to the entry set and then compute the new max
				entrySet.add(vehicle);
				int maxPriority = max();
				int vehiclePriority = vehicle.getVehiclePriority();
				
				// If the vehicle has the highest priority
				if (vehiclePriority >= maxPriority) {
					
					// Go through the tunnels and see whether the vehicle can enter any of them
					for (Tunnel tunnel : tunnels) {
						
						// If tryToEnter returns true, the vehicle has been entered into a tunnel.
						if (tunnel.tryToEnter(vehicle)) {
							
							// Remove the vehicle from the entry set
							this.entrySet.remove(vehicle);

							// Put the vehicle and the tunnel it entered in a hash map
							this.tMap.put(vehicle, tunnel);
							return tunnel;
						}
					}
				}	
				// If the vehicle could not be added to a tunnel they should wait
				this.wait();
			}
		} catch (InterruptedException e) {
		}
		return null;
	}
	
	/**
	 * Finds the maximum priority of the vehicles in the set
	 * @return int: max, the maximum priority
	 */
	public int max() {
		int max = -1;
		for (Vehicle vehicle : this.entrySet) {
			int vPriority = vehicle.getVehiclePriority();
			if (vPriority > max) {
				max = vPriority;
			}
		}
		return max;
	}
	
	
	@Override
	/**
	 * The method causes a vehicle to exit a tunnel and then notifies all the other vehicles that a tunnel is open.
	 * @param vehicle, the vehicle that is exiting
	 */
	public synchronized void exit(Vehicle vehicle) {
		Tunnel t = tMap.get(vehicle);
		t.exitTunnel(vehicle);
		notifyAll();
	}
	
}


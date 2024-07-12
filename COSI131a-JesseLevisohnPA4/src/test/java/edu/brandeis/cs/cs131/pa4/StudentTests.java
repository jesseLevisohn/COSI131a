package edu.brandeis.cs.cs131.pa4;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;

import edu.brandeis.cs.cs131.pa4.logging.Event;
import edu.brandeis.cs.cs131.pa4.logging.EventType;
import edu.brandeis.cs.cs131.pa4.logging.Log;
import edu.brandeis.cs.cs131.pa4.scheduler.Scheduler;
import edu.brandeis.cs.cs131.pa4.submission.PreemptivePriorityScheduler;
import edu.brandeis.cs.cs131.pa4.submission.PreemptiveTunnel;
import edu.brandeis.cs.cs131.pa4.submission.Vehicle;
import edu.brandeis.cs.cs131.pa4.tunnel.Ambulance;
import edu.brandeis.cs.cs131.pa4.tunnel.Car;
import edu.brandeis.cs.cs131.pa4.tunnel.Direction;
import edu.brandeis.cs.cs131.pa4.tunnel.Sled;
import edu.brandeis.cs.cs131.pa4.tunnel.Tunnel;

public class StudentTests {

	private final String preemptivePrioritySchedulerName = "PREEMPTIVE_SCHEDULER";

	private static final int NUM_RUNS = 10;

	@BeforeEach
	public void setUp() {
		Tunnel.DEFAULT_LOG.clearLog();
	}

	@BeforeAll
	public static void broadcast() {
		System.out.printf("Running Priority Scheduler Tests using %s \n",
				TestUtilities.factory.getClass().getCanonicalName());
	}

	private Scheduler setupPreemptivePriorityScheduler(String name) {
		Collection<Tunnel> tunnels = new ArrayList<Tunnel>();
		tunnels.add(TestUtilities.factory.createNewPreemptiveTunnel(name));
		return TestUtilities.factory.createNewPreemptivePriorityScheduler(preemptivePrioritySchedulerName, tunnels);
	}

	private Scheduler setupPreemptivePrioritySchedulerTwoTunnels(String name1, String name2) {
		Collection<Tunnel> tunnels = new ArrayList<Tunnel>();
		tunnels.add(TestUtilities.factory.createNewPreemptiveTunnel(name1));
		tunnels.add(TestUtilities.factory.createNewPreemptiveTunnel(name2));
		return TestUtilities.factory.createNewPreemptivePriorityScheduler(preemptivePrioritySchedulerName, tunnels);
	}
	
	@RepeatedTest(10)
	@Timeout(30)
	public void PreemptivePriorityManyAmb() {
		System.out.println("");
		System.out.println("new iteration");
		System.out.println("");
		
		List<Thread> vehicleThreads = new ArrayList<Thread>();
		Scheduler preemptivePriorityScheduler = setupPreemptivePriorityScheduler(TestUtilities.mrNames[0]);
		// start 3 slow cars
		for (int i = 0; i < 3; i++) {
			Vehicle car = TestUtilities.factory.createNewCar(Integer.toString(i), Direction.NORTH);
			car.setSpeed(0);
			car.setScheduler(preemptivePriorityScheduler);
			car.start();
			vehicleThreads.add(car);
		}
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// start 4 fast ambulances
		for (int i = 0; i < 4; i++) {
			Vehicle ambulance = TestUtilities.factory.createNewAmbulance("AMB" + i,
					Direction.values()[i % Direction.values().length]);
			ambulance.setSpeed(9);
			ambulance.setScheduler(preemptivePriorityScheduler);
			ambulance.start();
			vehicleThreads.add(ambulance);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Thread t : vehicleThreads) {
			try {
				t.join();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		// make sure that nobody exits the tunnel until ambulances exit
		Tunnel.DEFAULT_LOG.addToLog(EventType.END_TEST);
		Log log = Tunnel.DEFAULT_LOG;
		Event currentEvent;
		Vehicle ambulances[] = new Vehicle[4];
		Vehicle cars[] = new Vehicle[3];
		for (int i = 0; i < 4; i++)
			ambulances[i] = null;
		for (int i = 0; i < 3; i++)
			cars[i] = null;
		int ambulancesLeft = 0;
		do {
			currentEvent = log.get();
			if (currentEvent.getEvent() == EventType.ENTER_SUCCESS && currentEvent.getVehicle() instanceof Ambulance) {
				switch (currentEvent.getVehicle().getVehicleName()) {
				case "AMB0":
					ambulances[0] = currentEvent.getVehicle();
					break;
				case "AMB1":
					ambulances[1] = currentEvent.getVehicle();
					break;
				case "AMB2":
					ambulances[2] = currentEvent.getVehicle();
					break;
				case "AMB3":
					ambulances[3] = currentEvent.getVehicle();
					break;
				default:
					assertTrue(false, "Wrong vehicle entered tunnel!");
					break;
				}
			}
			if (currentEvent.getEvent() == EventType.ENTER_SUCCESS && currentEvent.getVehicle() instanceof Car) {
				switch (currentEvent.getVehicle().getVehicleName()) {
				case "0":
					cars[0] = currentEvent.getVehicle();
					break;
				case "1":
					cars[1] = currentEvent.getVehicle();
					break;
				case "2":
					cars[2] = currentEvent.getVehicle();
					break;
				default:
					assertTrue(false, "Wrong vehicle entered tunnel!");
					break;
				}
			}
			if (currentEvent.getEvent() == EventType.LEAVE_START) {
				if (currentEvent.getVehicle() instanceof Car && ambulancesLeft < 4) {
					assertTrue(false,
							"Vehicle " + currentEvent.getVehicle() + " left tunnel while "
									+ ((4 - ambulancesLeft) == 1 ? " 1 ambulance was"
											: (4 - ambulancesLeft) + " ambulances were")
									+ " still running!");
				}
				if (currentEvent.getVehicle() instanceof Ambulance)
					ambulancesLeft++;
			}
//			System.out.println(currentEvent.toString());
		} while (!currentEvent.getEvent().equals(EventType.END_TEST));
		for (int i = 0; i < 4; i++)
			if (ambulances[i] == null)
				assertTrue(false, "Ambulances did not enter tunnel successfully!");
		for (int i = 0; i < 3; i++)
			if (cars[i] == null)
				assertTrue(false, "Cars did not enter tunnel successfully!");
	}
}


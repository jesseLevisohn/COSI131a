package edu.brandeis.cs.cs131.pa4.submission;

import edu.brandeis.cs.cs131.pa4.tunnel.Tunnel;

/**
 * The BasicTunnel enforces a basic admittance policy.
 * It extends the Tunnel class.
 * 
 * You do not need to implement this class in PA4, but copying your solution from PA3 may be useful
 * for implementation of the PreemptiveTunnel.
 */
public class BasicTunnel extends Tunnel {

	/**
	 * Creates a new instance of a basic tunnel with the given name
	 * @param name the name of the basic tunnel
	 */
	public BasicTunnel(String name) {
		super(name);
	}

	@Override
	protected boolean tryToEnterInner(Vehicle vehicle) {
		throw new UnsupportedOperationException("UNIMPLEMENTED");
		
	}

	
	@Override
	protected void exitTunnelInner(Vehicle vehicle) {
		throw new UnsupportedOperationException("UNIMPLEMENTED");
	}
}

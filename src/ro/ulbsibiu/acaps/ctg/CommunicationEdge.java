package ro.ulbsibiu.acaps.ctg;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * {@link DefaultWeightedEdge} which has the communication volume as weight.
 * 
 * @author cipi
 * 
 */
public class CommunicationEdge extends DefaultWeightedEdge {

	private double volume;

	public CommunicationEdge() {
		super();
	}

	public void setWeight(double volume) {
		this.volume = volume;
	}

	@Override
	protected double getWeight() {
		return volume;
	}

	@Override
	public String toString() {
		return Long.toString((long)volume);
	}

}
package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Distance {

	int distance;

	protected Distance() {
	}

	public Distance(int distance) {
		this.distance = distance;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int subtract(int distance) {
		if(this.distance <= distance){
			throw new RuntimeException();
		}
		return this.distance - distance;
	}

	public int plus(int distance){
		return this.distance + distance;
	}
}
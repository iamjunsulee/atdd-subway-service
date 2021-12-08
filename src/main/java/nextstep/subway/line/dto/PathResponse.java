package nextstep.subway.line.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private Fare fare;

    private PathResponse() {
    }

    public PathResponse(GraphPath<Station, DefaultWeightedEdge> graphPath) {
        this.stations = graphPath.getVertexList()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        this.distance = (int) graphPath.getWeight();
        this.fare = Fare.ofByDistance(this.distance);
    }

    public List<StationResponse> getStations() {
        return new ArrayList<>(stations);
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare.getFare();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathResponse that = (PathResponse) o;
        return distance == that.distance && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance);
    }
}

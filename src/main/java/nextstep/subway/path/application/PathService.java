package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Money;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infra.JgraphtPathFinder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {

    private final PathRepository pathRepository;

    private final LineRepository lineRepository;

    private final PathFinder pathFinder = JgraphtPathFinder.getInstance();

    private final DistanceFee distanceFee;

    private final LineFee lineFee;

    private final MemberDiscount memberDiscount;

    public PathResponse findShortest(final long sourceId, final long targetId) {
        Path shortest = findShortestPath(sourceId, targetId);
        return PathResponse.of(shortest);
    }

    public PathResponse findShortestWithFee(final long sourceId, final long targetId, final LoginMember loginMember) {
        Path shortest = findShortestPath(sourceId, targetId);
        Money fee = settleFee(shortest, loginMember);
        return PathResponse.of(shortest, fee);
    }

    private Path findShortestPath(final long sourceId, final long targetId) {
        PathSections allSections = pathRepository.findAllSections();
        PathStation source = findById(sourceId);
        PathStation target = findById(targetId);
        return pathFinder.findShortest(allSections, source, target);
    }

    private Money settleFee(final Path path, final LoginMember loginMember) {
        Money settledDistanceFee = path.settle(distanceFee);
        Money settledLineFee = settleLineFee(path);
        Money totalFee = settledDistanceFee.add(settledLineFee);
        Money discount = memberDiscount.discount(loginMember, totalFee);
        return totalFee.subtract(discount);
    }

    private Money settleLineFee(final Path path) {
        List<Line> lines = lineRepository.findAllByIds(path.getLineIds());
        return lineFee.settle(lines);
    }

    private PathStation findById(final long targetId) {
        return pathRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
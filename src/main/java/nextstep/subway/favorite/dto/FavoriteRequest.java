package nextstep.subway.favorite.dto;

public class FavoriteRequest {
    private final long sourceId;
    private final long targetId;

    public FavoriteRequest(final long sourceId, final long targetId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public long getSourceId() {
        return sourceId;
    }

    public long getTargetId() {
        return targetId;
    }
}
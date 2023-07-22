package subway.dto.response;

import subway.domain.Section;

public class SectionResponse {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionResponse(Long id, Long lineId, Long upStationId, Long downStationId,
                           Long distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionResponse() {
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getLineId(),
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

}

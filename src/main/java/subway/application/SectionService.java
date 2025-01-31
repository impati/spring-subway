package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.*;
import subway.dto.request.SectionRequest;

@Service
public class SectionService {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public SectionService(final StationDao stationDao,
                          final LineDao lineDao,
                          final SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void createFirstSection(final Long lineId,
                                   final Long upStationId,
                                   final Long downStationId,
                                   final Long distance) {
        validateFirstSection(lineId);
        sectionDao.insert(createSection(lineId, upStationId, downStationId, distance));
    }

    private void validateFirstSection(final Long lineId) {
        if (sectionDao.existSectionByLineId(lineId)) {
            throw new IllegalStateException("노선에 구간이 존재하면 생성할 수 없습니다.");
        }
    }

    @Transactional
    public void createSection(final Long lineId, final SectionRequest request) {
        final Section newSection = createSection(
                lineId,
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());
        LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        lineSections.addSection(newSection);
        sectionDao.updateSections(lineId, lineSections);
    }

    private Section createSection(final Long lineId,
                                  final Long upStationId,
                                  final Long downStationId,
                                  final Long distance) {
        final Line line = findLineById(lineId);
        final Station upStation = findStationById(upStationId);
        final Station downStation = findStationById(downStationId);
        return new Section(line, upStation, downStation, new Distance(distance));
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        final Station station = findStationById(stationId);
        final LineSections lineSections = new LineSections(sectionDao.findAllByLineId(lineId));
        lineSections.deleteSection(station);
        sectionDao.updateSections(lineId, lineSections);
    }

    private Station findStationById(final Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("역 찾을 수 없습니다."));
    }

    private Line findLineById(final Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다."));
    }
}

package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.request.CreateLineRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.LineWithStationsResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;

    private final StationService stationService;

    private final SectionService sectionService;

    public LineService(final LineDao lineDao,
                       final StationService stationService,
                       final SectionService sectionService) {
        this.lineDao = lineDao;
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @Transactional
    public LineResponse saveLine(final CreateLineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));

        sectionService.saveFirstSection(
                persistLine.getId(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());

        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        return findLines().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    private List<Line> findLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public LineWithStationsResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        final List<Station> stations = stationService.findStationByLineId(id);
        return LineWithStationsResponse.of(persistLine, stations);
    }

    private Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    @Transactional
    public void updateLine(final Long id, final LineUpdateRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}


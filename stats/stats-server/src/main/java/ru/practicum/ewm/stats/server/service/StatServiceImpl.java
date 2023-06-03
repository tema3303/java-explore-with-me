package ru.practicum.ewm.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.model.Hit;
import ru.practicum.ewm.stats.server.model.HitMapper;
import ru.practicum.ewm.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatServiceImpl implements StatsService {

    private final HitRepository hitRepository;

    @Override
    @Transactional
    public HitDto createHit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        return HitMapper.toHitDto(hitRepository.save(hit));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uries, Boolean unique) {
        List<StatsDto> stats;
        if (uries == null || uries.isEmpty()) {
            if (unique) {
                stats = hitRepository.findAllUniqueIpWithoutUries(start, end);
            } else {
                stats = hitRepository.findAllWithoutUries(start, end);
            }
        } else {
            if (unique) {
                stats = hitRepository.findAllUniqueIpWithUries(uries, start, end);
            } else {
                stats = hitRepository.findAllWithUries(uries, start, end);
            }
        }
        return stats;
    }
}
package ru.practicum.ewm.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.dto.StatsDto;
import ru.practicum.ewm.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(" SELECT new ru.practicum.ewm.stats.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT h.ip))  " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<StatsDto> findAllUniqueIpWithoutUris(LocalDateTime start, LocalDateTime end);

    @Query(" SELECT new ru.practicum.ewm.stats.dto.StatsDto(h.app, h.uri, COUNT(DISTINCT h.ip))  " +
            "FROM Hit h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (DISTINCT h.ip) DESC")
    List<StatsDto> findAllUniqueIpWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(" SELECT new ru.practicum.ewm.stats.dto.StatsDto(h.app, h.uri, COUNT(h.uri))  " +
            "FROM Hit h " +
            "WHERE h.uri IN (?1) AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.uri) DESC")
    List<StatsDto> findAllWithUris(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query(" SELECT new ru.practicum.ewm.stats.dto.StatsDto(h.app, h.uri, COUNT(h.ip))  " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT (h.ip) DESC")
    List<StatsDto> findAllWithoutUris(LocalDateTime start, LocalDateTime end);
}
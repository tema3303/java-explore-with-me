package ru.practicum.ewm.stats.server.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.stats.dto.HitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(hitDto.getTimestamp())
                .build();
    }
}
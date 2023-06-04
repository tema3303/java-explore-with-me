package ru.practicum.ewm.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats.dto.HitDto;
import ru.practicum.ewm.stats.client.client.BaseClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class HitClient extends BaseClient {

    @Autowired
    public HitClient(@Value("${stats-client.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<HitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uries, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uries,
                "unique", unique
        );
        ResponseEntity<Object> objectResponseEntity =
                get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        List<HitDto> hitDto = new ObjectMapper().convertValue(objectResponseEntity.getBody(), new TypeReference<>() {
        });
        return hitDto;
    }

    public ResponseEntity<Object> createHit(HitDto hitDto) {
        return post("/hit", hitDto);
    }
}
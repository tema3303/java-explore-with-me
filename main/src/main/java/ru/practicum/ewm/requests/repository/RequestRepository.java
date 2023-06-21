package ru.practicum.ewm.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.requests.model.ParticipationRequest;
import ru.practicum.ewm.requests.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequester_Id(Long userId);

    ParticipationRequest findByIdAndRequesterId(Long requestId, Long userId);

    Boolean existsByRequester_IdAndEvent_Id(Long requestId, Long userId);
    List<ParticipationRequest> findAllByEvent_Id(Long eventId);
    List<ParticipationRequest> findAllByEventIdAndIdIn(Long eventId,List <Long> requestId);

}

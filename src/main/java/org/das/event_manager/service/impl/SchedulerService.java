package org.das.event_manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.das.event_manager.domain.*;
import org.das.event_manager.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);
    private final EventService eventService;
    private final EventKafkaProducerService eventKafkaProducerService;

    @Scheduled(cron = "${event.stats.cron}")
    public void updateEventStatuses() {
        log.info("EventStatus Scheduled Updater started");
        List<Long> startedEventIds = updateEventsToStarted();
        log.info("EventStatus for WAIT_START Updated on STARTED eventIds={}",startedEventIds);
        sendEventStatusUpdatesToKafka(startedEventIds, EventStatus.WAIT_START);
        List<Long> finishedEventIds = updateEventsToFinished();
        log.info("EventStatus for STARTED Updated FINISHED eventIds={}",finishedEventIds);
        sendEventStatusUpdatesToKafka(finishedEventIds, EventStatus.STARTED);
        log.info("EventStatus Scheduled Updater end");
    }

    private List<Long> updateEventsToStarted() {
            log.info("Change Events from WAIT_START to STARTED");
            return eventService.changeEventStatuses(EventStatus.STARTED);
        }

    private List<Long> updateEventsToFinished() {
            log.info("Change Events from STARTED to FINISHED");
           return eventService.changeEventStatuses(EventStatus.FINISHED);
        }

    private void sendEventStatusUpdatesToKafka(
            List<Long> events,
            EventStatus OldStatus
    ) {
        log.info("Begin send kafka event message for change events statuses to STARTED or FINISHED: events = {}",
                events);
        eventService.findAllByIds(events)
                .forEach(eventFound -> eventKafkaProducerService.sendEvent(
                        EventChangeKafkaMessage.builder()
                                .eventId(eventFound.id())
                                .ownerEventId(eventFound.ownerId())
                                .status(new EventFieldGeneric<>(OldStatus, eventFound.status()))
                                .registrationsOnEvent(eventFound.registrations()
                                        .stream()
                                        .map(EventRegistration::id).toList())
                                .build()
                ));
    }
}

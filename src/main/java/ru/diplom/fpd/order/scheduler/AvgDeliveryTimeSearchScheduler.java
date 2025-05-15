package ru.diplom.fpd.order.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.diplom.fpd.order.mapper.AverageDeliveryTimeMapper;
import ru.diplom.fpd.order.repository.AverageDeliveryTimeRepository;
import ru.diplom.fpd.order.service.OrderKafkaService;

@Service
@RequiredArgsConstructor
public class AvgDeliveryTimeSearchScheduler {

    private final AverageDeliveryTimeRepository averageDeliveryTimeRepository;
    private final OrderKafkaService orderKafkaService;
    private final AverageDeliveryTimeMapper averageDeliveryTimeMapper;

    @Scheduled(cron = "${schedulers.avf-delivery-time-update.cron}")
    public void courierSearchJob() {
        orderKafkaService.sendDeliveryTimeMessage(averageDeliveryTimeRepository.findAll().stream()
                .map(averageDeliveryTimeMapper::toDto)
                .toList());
    }

}

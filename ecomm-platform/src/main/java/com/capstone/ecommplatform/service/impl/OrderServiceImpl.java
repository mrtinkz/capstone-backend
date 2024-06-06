package com.capstone.ecommplatform.service.impl;

import com.capstone.ecommplatform.domain.Order;
import com.capstone.ecommplatform.repository.OrderRepository;
import com.capstone.ecommplatform.service.OrderService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.capstone.ecommplatform.domain.Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        log.debug("Request to save Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        log.debug("Request to update Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> partialUpdate(Order order) {
        log.debug("Request to partially update Order : {}", order);

        return orderRepository
            .findById(order.getId())
            .map(existingOrder -> {
                if (order.getShopperId() != null) {
                    existingOrder.setShopperId(order.getShopperId());
                }
                if (order.getStatus() != null) {
                    existingOrder.setStatus(order.getStatus());
                }
                if (order.getScheduledDeliveryDate() != null) {
                    existingOrder.setScheduledDeliveryDate(order.getScheduledDeliveryDate());
                }
                if (order.getVehicleId() != null) {
                    existingOrder.setVehicleId(order.getVehicleId());
                }
                if (order.getDealerId() != null) {
                    existingOrder.setDealerId(order.getDealerId());
                }

                return existingOrder;
            })
            .map(orderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}

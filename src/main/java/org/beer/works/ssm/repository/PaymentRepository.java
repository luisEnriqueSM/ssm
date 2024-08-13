package org.beer.works.ssm.repository;

import org.beer.works.ssm.domain.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEvent, Long> {
}

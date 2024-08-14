package org.beer.works.ssm.services;

import org.beer.works.ssm.domain.Payment;
import org.beer.works.ssm.domain.PaymentEvent;
import org.beer.works.ssm.domain.PaymentState;
import org.beer.works.ssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .amount(new BigDecimal("12.99"))
                .build();
    }

    @Transactional
    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        System.out.println("Before: " + savedPayment);

        StateMachine<PaymentState, PaymentEvent> sm = paymentService.preAuth(savedPayment.getId());

        Payment preAuthedPayment = paymentRepository.getReferenceById(savedPayment.getId());

        System.out.println(sm.getState().getId());

        System.out.println("After: " + preAuthedPayment);
    }

    @Transactional
    @RepeatedTest(10)
    //@Test
    void testAuth() {
        Payment savedPayment = paymentService.newPayment(payment);

        StateMachine<PaymentState, PaymentEvent> preAuthSM = paymentService.preAuth(savedPayment.getId());

        if (preAuthSM.getState().getId() == PaymentState.PRE_AUTH) {
            System.out.println("Payment is Pre Authorized");

            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(savedPayment.getId());

            System.out.println("Result of Auth: " + authSM.getState().getId());
        } else {
            System.out.println("Payment failed pre-auth...");
        }
    }
}
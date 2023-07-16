package guru.framework.msscssm.services;

import guru.framework.msscssm.domain.Payment;
import guru.framework.msscssm.domain.PaymentEvent;
import guru.framework.msscssm.domain.PaymentState;
import guru.framework.msscssm.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;
import java.util.Optional;

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
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Test
    void preAuth() {
        Payment savedPayment = paymentService.newPayment(payment);
        System.out.println("Should be NEW");
        System.out.println(savedPayment.getState());
        StateMachine<PaymentState, PaymentEvent> sm =  paymentService.preAuth(savedPayment.getId());
        Optional<Payment> preAuthPayment = paymentRepository.findById(savedPayment.getId());
        System.out.println("Should be PRE_AUTH");
        System.out.println(sm.getState().getId());
        System.out.println(preAuthPayment.get());
    }

    @RepeatedTest(10)
    void authorizePayment() {
        Payment savedPayment = paymentService.newPayment(payment);
        StateMachine<PaymentState, PaymentEvent> sm =  paymentService.preAuth(savedPayment.getId());
        if (sm.getState().getId() == PaymentState.PRE_AUTH) {
            System.out.println("Payment is Authorized");
            StateMachine<PaymentState, PaymentEvent> authSM = paymentService.authorizePayment(payment.getId());
            System.out.println("Result of Ath: "+authSM.getState().getId());
        } else {
            System.out.println("Payment failed pre-auth");
        }
    }
}
package guru.framework.msscssm.services;

import guru.framework.msscssm.domain.Payment;
import guru.framework.msscssm.domain.PaymentEvent;
import guru.framework.msscssm.domain.PaymentState;
import org.springframework.statemachine.StateMachine;

public interface PaymentService {
    Payment newPayment(Payment payment);
    StateMachine<PaymentState, PaymentEvent> preAuth(Long PaymentId);
    StateMachine<PaymentState, PaymentEvent> authorizePayment(Long PaymentId);
    StateMachine<PaymentState, PaymentEvent> declineAuth(Long PaymentId);
}

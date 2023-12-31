package guru.framework.msscssm.services;

import guru.framework.msscssm.domain.Payment;
import guru.framework.msscssm.domain.PaymentEvent;
import guru.framework.msscssm.domain.PaymentState;
import guru.framework.msscssm.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static guru.framework.msscssm.services.PaymentServiceImpl.PAYMENT_ID_HEADER;

@RequiredArgsConstructor
@Component
public class PaymentStateChangeInterceptor extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent> {

    private final PaymentRepository paymentRepository;

    @Override
    public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message, Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine, StateMachine<PaymentState, PaymentEvent> rootStateMachine) {
        Optional.ofNullable(message).ifPresent(msg ->
                Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(PAYMENT_ID_HEADER, -1L)))
                        .ifPresent(paymentId -> {
                                    Payment payment = paymentRepository.findById(paymentId).get();
                                    payment.setState(state.getId());
                                    paymentRepository.save(payment);
                                })
                );
    }
}

package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.Events;
import com.example.springstatemachine.domain.States;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

@Slf4j
@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

  @Override
  public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {

    config.withConfiguration()
        .listener(listener())
        .autoStartup(true);
  }

  @Override
  public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {

    states.withStates()
        .initial(States.BACKLOG)
        .state(States.IN_PROGRESS)
        .state(States.TESTING)
        .end(States.DONE);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {

    transitions.withExternal()
        .source(States.BACKLOG)
        .target(States.IN_PROGRESS)
        .event(Events.START_FEATURE)
        .and()
        .withExternal()
        .source(States.IN_PROGRESS)
        .target(States.TESTING)
        .event(Events.FINISH_FEATURE)
        .and()
        .withExternal()
        .source(States.TESTING)
        .target(States.IN_PROGRESS)
        .event(Events.QA_TEAM_REJECT)
        .and()
        .withExternal()
        .source(States.TESTING)
        .target(States.DONE)
        .event(Events.QA_TEAM_APPROVE);
  }

  private StateMachineListener<States, Events> listener() {
    return new StateMachineListenerAdapter<States, Events>() {
      @Override
      public void transition(Transition<States, Events> transition) {
        log.warn("move from:{} to:{}",
            ofNullableState(transition.getSource()),
            ofNullableState(transition.getTarget()));
      }

      @Override
      public void eventNotAccepted(Message<Events> event) {
        log.error("event not accepted: {}", event);
      }

      private Object ofNullableState(State s) {
        return Optional.ofNullable(s)
            .map(State::getId)
            .orElse(null);
      }
    };
  }
}

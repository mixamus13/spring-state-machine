package com.example.springstatemachine.config;

import com.example.springstatemachine.domain.Events;
import com.example.springstatemachine.domain.States;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<States, Events> {

  @Override
  public void configure(
      StateMachineConfigurationConfigurer
          <States, Events> config) throws Exception {

    config.withConfiguration()
        .autoStartup(true);
  }

  @Override
  public void configure(
      StateMachineStateConfigurer<States, Events> states)
      throws Exception {

    states.withStates()
        .initial(States.BACKLOG)
        .state(States.IN_PROGRESS)
        .state(States.TESTING)
        .end(States.DONE);
  }

  @Override
  public void configure(
      StateMachineTransitionConfigurer<States, Events> transitions)
      throws Exception {

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
}

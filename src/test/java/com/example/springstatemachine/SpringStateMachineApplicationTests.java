package com.example.springstatemachine;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springstatemachine.domain.Events;
import com.example.springstatemachine.domain.States;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

@SpringBootTest
class SpringStateMachineApplicationTests {

  @Autowired
  private StateMachine<States, Events> stateMachine;

  @Test
  void initTest() {

    assertThat(stateMachine.getState().getId()).
        isEqualTo(States.BACKLOG);
  }

  @Test
  void testGreenFlow() {
    // Arrange & Act
    stateMachine.sendEvent(Events.START_FEATURE);
    stateMachine.sendEvent(Events.FINISH_FEATURE);
    stateMachine.sendEvent(Events.QA_TEAM_APPROVE);

    // Assert
    assertThat(stateMachine.getState().getId())
        .isEqualTo(States.DONE);
  }

  @Test
  void testWrongWay() {
    // Arrange & Act
    stateMachine.sendEvent(Events.START_FEATURE);
    stateMachine.sendEvent(Events.QA_TEAM_APPROVE);
    // Asserts
    assertThat(stateMachine.getState().getId())
        .isEqualTo(States.IN_PROGRESS);
  }
}

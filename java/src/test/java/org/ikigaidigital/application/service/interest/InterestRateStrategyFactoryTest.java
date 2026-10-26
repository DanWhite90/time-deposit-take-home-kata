package org.ikigaidigital.application.service.interest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class InterestRateStrategyFactoryTest {

  private final InterestRateStrategyFactory factory = new InterestRateStrategyFactory();

  @Test
  void returnsOneSingletonPerPlanType() {
    assertThat(factory.forPlanType("basic"))
        .isSameAs(factory.forPlanType("basic"))
        .isInstanceOf(BasicInterestRateStrategy.class);
    assertThat(factory.forPlanType("student"))
        .isSameAs(factory.forPlanType("student"))
        .isInstanceOf(StudentInterestRateStrategy.class);
    assertThat(factory.forPlanType("premium"))
        .isSameAs(factory.forPlanType("premium"))
        .isInstanceOf(PremiumInterestRateStrategy.class);
    assertThat(factory.forPlanType("unknown"))
        .isSameAs(factory.forPlanType("other"))
        .isInstanceOf(NoOpInterestRateStrategy.class);

    assertThat(factory.forPlanType("basic"))
        .isNotSameAs(factory.forPlanType("student"))
        .isNotSameAs(factory.forPlanType("premium"))
        .isNotSameAs(factory.forPlanType("unknown"));
  }

  @ParameterizedTest(name = "unrecognized plan type \"{0}\"")
  @MethodSource("unrecognizedPlanTypes")
  void unrecognizedPlanTypesUseTheNoOpSingleton(String planType) {
    assertThat(factory.forPlanType(planType))
        .isSameAs(factory.forPlanType("unknown"))
        .isInstanceOf(NoOpInterestRateStrategy.class);
  }

  private static Stream<Arguments> unrecognizedPlanTypes() {
    return Stream.of(
        arguments("unknown"),
        arguments("Basic"),
        arguments("STUDENT"),
        arguments("Premium"),
        arguments("premium "),
        arguments("student "));
  }
}

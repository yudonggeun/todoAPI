package com.example.todo.practice;

import com.example.todo.dto.SampleDto;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.FixtureMonkeyBuilder;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.assertj.core.api.BDDAssertions.then;

public class FixtureMonkeyTest {

    @DisplayName("giveMeOne을 통한 픽스터 생성")
    @RepeatedTest(10)
    void test() {
        FixtureMonkeyBuilder builder = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .plugin(new JakartaValidationPlugin());

        FixtureMonkey fixtureMonkey = builder.build();

        var sample = fixtureMonkey.giveMeOne(SampleDto.class);

        System.out.println(sample);
        then(sample).isNotNull();
    }

    @DisplayName("giveMeBuilder를 통한 픽스터 생성")
    @RepeatedTest(10)
    void test2() {
        FixtureMonkeyBuilder builder = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .plugin(new JakartaValidationPlugin());

        FixtureMonkey fixtureMonkey = builder.build();

        ArbitraryBuilder<SampleDto> objectBuilder = fixtureMonkey.giveMeBuilder(SampleDto.class);
        var sample = objectBuilder.sample();

        System.out.println(sample);
        then(objectBuilder).isNotNull();
    }

    @DisplayName("giveMeBuilder를 통해서 특정 조건(setPostCondition)에 맞는 픽스터 생성")
    @RepeatedTest(10)
    void test3() {
        FixtureMonkeyBuilder builder = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .plugin(new JakartaValidationPlugin());

        FixtureMonkey fixtureMonkey = builder.build();

        ArbitraryBuilder<SampleDto> objectBuilder = fixtureMonkey.giveMeBuilder(SampleDto.class);
        var sample = objectBuilder
                .setPostCondition("password", String.class, it -> it.length() < 10)
                .sample();

        System.out.println(sample);
        then(objectBuilder).isNotNull();
    }

    @DisplayName("giveMeBuilder를 통해서 필드 설정(set)에 맞는 픽스터 생성")
    @RepeatedTest(10)
    void test4() {

        FixtureMonkeyBuilder builder = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .plugin(new JakartaValidationPlugin());

        FixtureMonkey fixtureMonkey = builder.build();

        ArbitraryBuilder<SampleDto> objectBuilder = fixtureMonkey.giveMeBuilder(SampleDto.class);
        var sample = objectBuilder
                .set("password", "1234Daaee")
                .sample();

        System.out.println(sample);
        then(objectBuilder).isNotNull();
    }
}

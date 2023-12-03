package com.example.todo;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public class TestSupport {

    protected FixtureMonkey constructFixture;
    protected FixtureMonkey builderFixture;

    public TestSupport() {
        this.constructFixture = FixtureMonkey.builder()
                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .defaultNotNull(true)
                .plugin(new JakartaValidationPlugin()).build();

        var builderArbitraryIntrospector = BuilderArbitraryIntrospector.INSTANCE;
        builderArbitraryIntrospector.setDefaultBuilderMethodName("builder");
        builderArbitraryIntrospector.setDefaultBuildMethodName("build");

        this.builderFixture = FixtureMonkey.builder()
                .objectIntrospector(builderArbitraryIntrospector)
                .defaultNotNull(true)
                .plugin(new JakartaValidationPlugin()).build();
    }
}

/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.r2dbc;

import javax.sql.DataSource;

import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link EmbeddedDatabaseConfiguration}.
 *
 * @author Mark Paluch
 */
class EmbeddedDatabaseConfigurationTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(ConnectionFactoryAutoConfiguration.class));

	@Test
	void testDefaultUnpooledConnectionFactoryExists() {
		this.contextRunner.run((context) -> assertThat(context).hasSingleBean(ConnectionFactory.class));
	}

	@Test
	void defaultConnectionFactoryIsUnpooled() {
		this.contextRunner.run((context) -> {
			ConnectionFactory connectionFactory = context.getBean(ConnectionFactory.class);
			assertThat(connectionFactory).isExactlyInstanceOf(H2ConnectionFactory.class);
			assertThat(context).doesNotHaveBean(DataSource.class);
		});
	}

	@Test
	void testWithoutSpringJdbc() {
		this.contextRunner.withClassLoader(new FilteredClassLoader("org.springframework.jdbc"))
				.run((context) -> assertThat(context).hasSingleBean(ConnectionFactory.class));
	}

}

package de.johanneswirth.apps.auth

import de.johanneswirth.apps.auth.services._
import de.johanneswirth.apps.common.{ContainerFilter, VerificationHelper}
import io.dropwizard.Application
import io.dropwizard.configuration.{EnvironmentVariableSubstitutor, SubstitutingSourceProvider}
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jdbi3.JdbiFactory
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.{Bootstrap, Environment}

object AuthServiceApp extends Application[AuthConfiguration] {
  def main(args: Array[String]): Unit = AuthServiceApp.run(args:_*)

  override def initialize(bootstrap: Bootstrap[AuthConfiguration]): Unit = {
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider, new EnvironmentVariableSubstitutor(true)))
    bootstrap.addBundle(new MigrationsBundle[AuthConfiguration]() {
      override def getDataSourceFactory(configuration: AuthConfiguration): DataSourceFactory = configuration.database
    })
  }

  override def run(config: AuthConfiguration, environment: Environment): Unit = {
    environment.jersey.register(new JsonProcessingExceptionMapper(true))
    val utils = new PasswordUtils(config.publicKey, config.privateKey)
    environment.lifecycle.manage(utils)
    val helper = new VerificationHelper(config.publicKey)
    environment.lifecycle.manage(helper)
    val factory = new JdbiFactory
    val jdbi = factory.build(environment, config.database, "mysql")
    jdbi.registerRowMapper(new PairMapperFactory)
    environment.jersey.register(new LoginService(jdbi, utils))
    environment.jersey.register(new RegisterService(jdbi, utils, config))
    environment.jersey.register(new PasswordChangeService(jdbi, utils))
    environment.jersey.register(new PasswordRecoverService(jdbi, utils))
    environment.jersey.register(new UsersService(jdbi))
    environment.jersey.register(new ContainerFilter(helper))
  }
}
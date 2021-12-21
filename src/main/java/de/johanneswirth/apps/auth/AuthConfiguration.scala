package de.johanneswirth.apps.auth

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class AuthConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty
  var publicKey: String = _

  @NotEmpty
  @JsonProperty
  var privateKey: String = _

  @JsonProperty
  var requireInvitation = true

  @Valid
  @NotNull
  @JsonProperty
  var database: DataSourceFactory = new DataSourceFactory
}
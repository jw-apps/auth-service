#!/bin/sh
#/tac-auth-service/bin/tac-auth-service db status config.yml
#mkdir dumps
#/tac-auth-service/bin/tac-auth-service db dump config.yml > "dumps/$(date +%Y-%m-%d_%H-%M-%S)"
#/tac-auth-service/bin/tac-auth-service db tag config.yml "$(date +%Y-%m-%d_%H-%M-%S)"
/apps-auth-service/bin/apps-auth-service db migrate config.yml
#/tac-auth-service/bin/tac-auth-service db status config.yml
/apps-auth-service/bin/apps-auth-service server config.yml


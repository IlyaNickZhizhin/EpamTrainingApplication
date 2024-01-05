package org.epam.gymservice.config.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TraineeHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        int errorCode = isEverythingOk() ? 0 : 1;
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private boolean isEverythingOk() {
        return true;
    }
}

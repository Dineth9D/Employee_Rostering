package com.employee_rostering;

import org.optaplanner.core.api.solver.SolverConfig;
import org.optaplanner.core.api.solver.SolverManager;
import org.optaplanner.core.config.solver.SolverManagerConfig;
import org.optaplanner.core.impl.solver.DefaultSolverManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OptaPlannerConfig {

    @Bean(name = "solverManager")
    public SolverManager<?, ?> solverManager() {
        final SolverConfig solverConfig = new SolverConfig(); // Create or load your SolverConfig
        final SolverManagerConfig solverManagerConfig = new SolverManagerConfig(); // Create or load your SolverManagerConfig
        return SolverManager.create(solverConfig, solverManagerConfig);
    }

}


package com.employee_rostering;

import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OptaPlannerSolver {

    private SolverManager<EmployeeRoster, Long> solverManager;

    @Autowired
    public OptaPlannerSolver(SolverManager<EmployeeRoster, Long> solverManager) {
        this.solverManager = solverManager;
    }

    public void solve(EmployeeRoster employeeRoster) {
        solverManager.solveAndListen(1L, solverJob -> {
            // Perform any necessary modifications to the employeeRoster object
            // before returning it as the solution
            return employeeRoster;
        }, bestSolution -> {
            // Process the bestSolution if needed
        });
    }
}
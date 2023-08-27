
# Employee Rostering System

The Employee Rostering System is a demonstration of an intelligent scheduling solution that optimizes employee rostering and scheduling. It utilizes the OptaPlanner framework to create efficient schedules based on various constraints and requirements.

## Features

- Constraint-based optimization for employee scheduling.
- Consideration of factors like employee availability, skills, and shift requirements.
- Integration with MongoDB Atlas as the database for data storage.
- Constraint satisfaction problem formulation using the OptaPlanner framework.
- Schedule generation with a `DataGenerator` component.
- Solver management for optimization process control.

## Getting Started

These instructions will help you set up and run the Employee Rostering System locally for development and testing purposes.

### Prerequisites

- Java JDK (version 17 or later)
- MongoDB Atlas account (for data storage)

### Installation

1. Clone the repository.
2. Configure your MongoDB Atlas credentials in the application's configuration files.
3. Run the application.

### Usage

1. Run the application.
2. Access the application at http://localhost:8080/schedule.

## Architecture

The system comprises several key components:

- Domain Models: Represent entities like `Employee`, `Availability`, `Shift`, and `ScheduleState`.
- Data Persistence: Utilizes MongoDB Atlas for data storage.
- Constraint-based Optimization: Uses OptaPlanner to solve the rostering problem.
- Solver Management: Manages the optimization process using `SolverManager`.

## Future Enhancements

The system can be further improved by:

- Incorporating machine learning techniques for schedule recommendations.
- Integrating with external systems for real-time data updates.


## Acknowledgments

- The system was inspired by real-world employee scheduling challenges.
- Thanks to the OptaPlanner community for providing the optimization framework.

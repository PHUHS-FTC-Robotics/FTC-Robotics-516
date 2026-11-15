
# FTC Robotics 516

## Todo list

- [ ] Refactor telemetry collection into a reusable `DataLogger` class
- [ ] Separate continuous telemetry data from individual shot-event data
- [ ] Add timestamps, run IDs, and shot IDs to the data structures
- [ ] Make sure telemetry column names accurately represent what is being measured
- [ ] Build a basic pandas workflow to validate and clean collected data
- [ ] Set up the notebook structure for data analysis and documentation
- [ ] Pull telemetry from each match and analyze what can be improved (drivetrain, shooter, battery voltage, etc.)
- [ ] Figure out Limelight dependencies

### After the new robot design is finalized

- [ ] Update hardware configuration and software architecture to account for mechanical changes
- [ ] Implement and test the new subsystems
- [ ] Implement shot-event recording through TeleOp
- [ ] Make sure individual shots can be connected to their corresponding telemetry measurements
- [ ] Verify that all data is being recorded correctly before beginning experiments

### After the robot is built

- [ ] Collect controlled shooter experiment data
- [ ] Record relevant variables for each shot (distance, target velocity, actual velocity, battery voltage, outcome, etc.)
- [ ] Collect enough observations to investigate relationships between variables
- [ ] Develop a regression model to investigate shooter performance
- [ ] Validate the model using separate testing sessions
- [ ] Use the results to inform shooter settings and potential mechanical/programming improvements

### Completed

- [x] Drivetrain with proper ports/functions
- [x] Pedro Pathing constants for PP3
- [x] Get sloth for faster code upload
- [x] Fix telemetry data structures into proper class
- [x] Get basic telemetry stats ready (wheel powers, battery voltage)
- [x] Pedro Pathing dependencies installed
- [x] Basic Java -> ArrayList -> CSV in `data` folder -> shown in notebook

**Note:** We can test the data logger with the current robot, but we should wait until the new robot is built and its measurements are finalized before collecting data for the actual regression experiments.

---

## What is this repository?

This repository contains the code and data analysis tools for FTC Robotics 516. It is separate from the upstream FTC SDK repository and serves as a place to organize our robot code, telemetry, and machine learning experiments.

## How to contribute

1. Create a new branch for your feature or changes.
2. Make your changes and test them.
3. Push your branch frequently so changes are backed up.
4. Open a pull request when your changes are ready.
5. Review the changes before merging into `main`.

Try to keep features separated into their own files/classes instead of putting everything into one large OpMode.

## How the code works

The main FTC code is located in:

`TeamCode/src/main/java/org/firstinspires/ftc/teamcode`

The project is organized into separate packages for hardware, subsystems, OpModes, and telemetry. This keeps hardware configuration and individual subsystem logic separate from the main TeleOp and autonomous routines.

The data analysis side is organized separately:

- `notebooks/` contains Jupyter notebooks for data cleaning, visualization, statistical analysis, and modeling.
- `data/` contains exported CSV telemetry and experiment data.
- `.venv/` contains the local Python virtual environment.

### Data collection

There are two main types of data we want to collect:

**1. Continuous telemetry**

This is sampled at regular intervals (currently around every 100 ms). It records how the robot is operating over time.

Examples:
- Drivetrain motor velocities/powers
- Shooter velocity
- Shooter target velocity
- Intake power
- Battery voltage
- Subsystem states

**2. Shot-event data**

This is recorded once for each individual shot, rather than at a fixed time interval.

Examples:
- Shot ID and timestamp
- Shooting distance
- Target shooter velocity
- Actual shooter velocity
- Battery voltage
- Velocity stability before the shot
- Ball type
- Whether the shot scored or missed
- Landing error, if it can be measured consistently

These two datasets can be connected using timestamps and shot IDs. This lets us investigate what the robot was doing immediately before and during a particular shot.

The Java code handles collecting and exporting the data. Python handles cleaning, visualization, statistical analysis, and modeling.

## Python environment

The project uses a local Python virtual environment. Activate it before running notebooks.

Install the required packages with:

```bash
pip install numpy pandas matplotlib seaborn
```

Additional libraries can be added as the project develops.

---

## But why Python data analysis?

There is a difference between making an engineering decision based on intuition and making one based on measured evidence.

For example, imagine that our shooter appears inconsistent during a match.

An intuition-based approach might be:

> The shooter seems slow, so let's increase its power.

That might work, but we don't actually know whether shooter velocity is the problem. Maybe battery voltage is dropping, the ball feeder is inconsistent, or the shooter hasn't reached its target velocity before a shot.

A data-driven approach would be to collect telemetry and investigate the problem.

We could compare actual shooter velocity, target velocity, battery voltage, and shot outcomes to see which variables are associated with successful shots.

This doesn't automatically tell us what causes a problem. However, it gives us evidence to develop better hypotheses and test potential solutions.

The goal isn't to replace engineering intuition. It's to use data to make that intuition more informed.

## Why use regression and data modeling?

Once we have collected enough reliable data, we can investigate whether certain variables help explain or predict shooter performance.

For example, we could investigate the relationship between:

- Shooting distance
- Target shooter velocity
- Actual shooter velocity
- Battery voltage
- Shooter velocity stability
- Ball type
- Turret or launch angle, depending on the final design

Depending on what we measure, we could use different types of regression.

**Logistic regression** could estimate the probability of a shot scoring based on the measured variables. The outcome would be binary: scored or missed.

**Linear regression** could be used if we can consistently measure something continuous, such as the landing error of a shot.

We can also investigate whether relationships are nonlinear or whether multiple variables interact with one another.

Of course, a model is only useful if it generalizes to new data. We will need to evaluate it using observations that were not used to train it, ideally from separate testing sessions.

The point isn't to build a complicated model just for the sake of using machine learning. It's to determine whether modeling actually helps us understand or improve the robot.

## What makes this project unique?

The interesting part is connecting actual robotics engineering with statistical learning.

Rather than working with a dataset someone else created, we are collecting measurements from a physical robot, investigating its performance, and using those results to inform future design decisions.

The general process is:

1. Identify a problem with the robot.
2. Develop a hypothesis about what might be causing it.
3. Determine what variables we need to measure.
4. Collect and analyze the data.
5. Build a statistical model if it is appropriate.
6. Validate the model using unseen data.
7. Use the results to inform a mechanical or programming change.
8. Test the change against the original performance.

This creates a feedback loop between mechanical engineering, programming, and data science.

The model doesn't make engineering decisions for us. It gives us another way to understand the robot and evaluate whether our changes actually improve its performance.

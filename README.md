# Todo list
Oldest tasks will be at the bottom of the list. For instructions on what to do with the project, see below.
- [ ] Drivetrain with proper ports and functions ready to go
- [ ] With drivetrain, make a simple program to make it so that when you run the robot, ensure all motors are at the same speed throughout program with Python data analysis and telemetry (this will be the first true run with Python that is not just dummy data)
- [ ] Pedro pathing constants (required continuous updating of robot's mass)
- [ ] Complete pedro pathing tuning
- [x] Pedro pathing dependencies installed
- [x] Basic Java -> ArrayList -> CSV in `data` folder -> shown in notebook (not with telemetry just yet)
We will go from there, these are top priority.

# Stuff to Know
Understand that this `README.md` file is specifically meant for this repository. The original repo for this fork has a different `README.md` that is more generalized for FTC Robotics [(see here)](https://github.com/FIRST-Tech-Challenge/FtcRobotController) and setting up the project. This `README.md` for this repo is meant to be a simple place to find information on how stuff works (especially with the data science implementations with Python) and how adding to this repo should happen.

## Adding to the repo
In order to contribute to this repo, make sure to always create a separate branch. Make sure the branch name is somewhat descriptive and is not just your name. NEVER push bad code to the `main` branch for whatever reason (you should not be able to anyways). When you think your code is ready, make a PR and it will be reviewed. So the process would be:
1. Make a new feature on a separate branch with a descriptive name
2. Push often to that branch so changes to code are easy to track
3. Test your changes. Make sure nothing crashes, and everything works as intended with your code
4. Once ready, make a PR with a detailed description of what is added
5. The PR will be tested and will eventually me merged to `main` if everything works

## How the code works
This project is not necessarily a typical FTC Robotics repo. There will be various separate folders at the project root with the purpose of collecting telemetry data for data science analysis with Python libraries. So the project structure will be a bit different: 

- all TeleOp, Auto, and Pedro Pathing code will live inside `TeamCode/src/main/java/org/firstinspires/ftc/teamcode`
- for these programs, they may be separated into different folders for organizational purposes
- we do have test programs for python data analysis right now, however they are in beginner stages (look below for exact file structures)
- in the future, we will take take certain data from telemetry at certain time intervals (like every 0.5 seconds) either in testing or matches, and use that data to continually improve our robot and its programming. Code will live in separate folders:
	- there will be a `notebooks` folder in the project root with all notebooks for testing specific things
   	- there will be a `data` folder where ArrayLists from telemetry will be converted into CSV files, so that Python can read the data
   	- you should a Python virtual environment folder called `.venv`, see below for specific instructions:

### Dependencies
Because this project involves Python, there will be a few required dependencies. Before doing any of the following installations, make sure:
1. You have Python and pip installed locally on your computer
2. Your Gradle build runs properly
3. You should have git installed already if you cloned this project, but still ensure you have it locally
Now, you will be creating a Python virtual environment. Follow these instructions and put these commands in the terminal:
```powershell
python -m venv .venv
# the following command will both activate the venv and start the localhost for editing notebooks (comes from a separate ps1 file)
.\activate.ps1
# if you recieve any errors, hit Ctrl+C and make sure you install the following:
pip install numpy pandas matplotlib seaborn # do this exactly (make sure you are in the venv)
```

### But why Python data analysis?
Notebook is arguably more important than competition performance itself. One of the most important parts of notebook is being able to test new changes with the engineering process. The goal of using Python and data science libraries like `numpy`, `pandas`, `matplotlib`, etc. is to differentiate the process of positive and normative engineering:

- Normative process involves looking at something and suggesting a change based on how something may appear. For example, if one wheel appears to be slower than another, you may try to artifically increase the speed of that wheel by guessing certain values for `setPower`.
- Positive process involves looking at a certain issue and fixing it with data. Using the example from above, if one wheel is slightly slower than others, you would use telemetry to find the ticks/sec of every single wheel and try to find any confounding variables affecting the speed of the wheel.

Note that neither of these processes are inherently bad. However, notebook with FTC has a heavy focus on data analysis, therefore it is incredibly crucial to have more positive forms of the engineering process in our notebook. These notebooks will likely include various `pandas` dataframes and `matplotlib` graphs to represent these dataframes.

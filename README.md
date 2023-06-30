## Group 05 - Multi-Modal Digital Assistant - @ v 3.0 (06-2023)
### An Assistant to your Everyday Life
###### ***Developed by***: Reda Belcaid, Sam Goldie, Haoran Luan, Kumar Sambhavit, Jesse Stettner, Elza Strazda, Tom Verbeek

This Application consists of a simple GUI that allows the user to interact with a virtual digital assistant.

Before running the application, a python virtual environment must be created in order for the machine learning models to run properly.
You can do so by running the following commands in the terminal:
### Create environment
```shell
python -m venv mmda_venv
```
### Activate environment
- Mac/Linux:
```shell
source mmda_venv/bin/activate
```
- Windows:
```shell
mmda_venv\Scripts\activate.bat
```
### Install requirements
```shell
python -m pip install -r  src/main/resources/py/requirements.txt
```

<br/><br/>You can run this application using Gradle and Java 17. Simply build the project and run the file [<u>`Run.java`</u>](src/main/java/Run.java). 
<br/>The application will start and you can interact with the assistant.
<br/><br/>The assistant is able to perform a variety of tasks, such as: 
- Simple Template Skills 
- CFG Skills

The user can interact with the assistant by typing in a GUI. To Choose between *Template Skills* and *CFG Skills* the user can navigate to the Settings Page. The user can also define new skills both for Template Skills and CFG Skills.
<br/>This can by editing the respective files in the Skill Editor Page.

To bypass facial recognition at login, use user "admin" and any password.
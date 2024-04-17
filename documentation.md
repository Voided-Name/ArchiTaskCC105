# Dashboard (Architect)
# How to Handle UI
So the issue is that I have a save button which means that changes is not automatically done to the database

## Initial UI Setup

uiSetup() 
- setups the width of the table for deliverables under the first project in the list
- setOnKeyPressed (CTRL + S) to save current changes 
- binds the UI to width for responsiveness
- setOnKeyPressed (Text Area for details) change to Text

setupProjectTreeView()
- get current projects
- insert information into different ArrayLists
- get current details
- insert information into different ArrayLists
- updates current TreeView

projectAdd()
- responsible for showing the UI when adding a new project 

addProject()
- responsible for handling a new project
- returns an error if the project already exists

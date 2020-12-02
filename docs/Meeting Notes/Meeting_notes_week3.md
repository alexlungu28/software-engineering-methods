##Sprint roles
Sprint master:
- Project leader 
- Allocate tasks
- Keep track of if everyone is doing well 
- And keep track of if progress is delivered.

Sprint secretary: Takes notes.

##General feedback
- Try to assert more how we are structuring our team
- Good coordination is important
- Sit together and distribute tasks
- Aim for around equal code collaboration
- List of tasks is well made, but add more details 

##Gitlab structure feedback:
- There is not too much feedback yet because there is little activity
- We should try to upload more code, more microservices, use it to monitor progress
- We should try to organise a bit more 
- There is not a lot of code committed yet, we should try to preserve equal amount of commits
- Aim for equal distribution of code & commits

###Tasks
- We can transfer tasks to the next week
- First add all tasks, assign people and then look what we can do each week
- Write down what the task is actually about like crud operations, or link to file with details
- Keep tasks as clean as possible

###Branches
- Advised to use feature to generate branches out of tasks
- Try to have different branches per feature

###Merge requests
- We should try to have good comments on them, and good feedback to improve feedback for next merge request

##Sprints
- We should add the sprint retrospective at the end of each sprint
- Additionally, upload pmd and checkstyle reports
- Along with those reports, upload a screenshot of test code coverage


##Questions: 
###Topic 1
- If authentication stores user info, then that does everything regarding the user
- Suggestion for division into microservices is: user, lecture and scheduler microservices
- Repeating data should be avoided
- Users and authentication could be merged
- If there is strong connection between two microservices then they probably should be combined
- Data between services need to be consistent.
- Microservices do not mean split everything, just splitting in domains
- Try to keep data in the same domain
- Lots of issues means we need to rethink microservice

###Topic 2
- We do not have to split application layer & other layers
- All layers are in one microservice
- Each microservice goes over 1 domain, only requests data if it needs it from other domains

###Topic 3
- We do not need to worry too much if it goes wrong, we need to improve in the rest of the project
- The project does not need to be perfected right now

###Topic 4: focus points
- For now the schedule is the biggest focus point
- We should have a demo next week!
- Endpoints and communicate with application
- We should have sprint master next week
- Rotate roles every week
- Put all microservices in a new folder called 'subprojects'

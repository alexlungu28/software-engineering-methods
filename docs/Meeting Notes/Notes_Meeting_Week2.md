# Notes – Meeting Week 1 (Ee Xuan Tan)
Feedback on requirements: 
General problems: 
- Must have, Could have, Should have: They generally should be equally distributed. Not everything has to be a must have. 
Things that are unique and what you need to solve the main problem. 
- Everything that is not so fundamental --> should have. Ones that are important but not defining. 
- Could haves --> optional. 

F.E. A student should be able to accept/decline an invitation --> not a must have 
Enrollment and unenrollment --> should have 

General remarks: 
-Adding, editing and delete is not so fundamental (dont really need a way to manage them. Something that has to be changed easily: percentage of capacity (configuration file is already enough). 
- Reconsider roles, teacher, users is already enough. First focus on scheduling, teachers need to say who needs to be in the course. 
- Didnt specify how the system would do the main task. Specify when the system would do it, How will u specify the rooms. At the beginning of the quarter? or each week? 
- Specify what info we need to store for rooms/teachers/students 
- Come up with more details (but not too many details). 
- It will be very helpful if you create user stories, write stories in the point of a teacher and a student. (These should be in a different document).
- Come up with non-functional requirement, how are u going to assess the quality (what tools), what database, what version of java, (come up with atleast 4 more). 
- if you want to edit lecture --> must reschedule everything. If you are going to schedule every 2 weeks, then only allow changes every 2 weeks.


Questions: 
We can assume there is a fix number of rooms? 
Tudelft rooms don't change, so yes

Can there be cases that it is impossible to allocate every student? 
Sometimes this might not be possible, then you do not allocate this. However you can try and give this student priority in the next week. 

Only parameter to change? 
Only the capacity. We can make some assumptions. 

Should we have a Development branch so that we first merge all branches there and then after making sure everything works we merge with master? 
Use a development branch.
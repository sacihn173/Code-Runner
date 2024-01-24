# Code-Runner

Application to run code and get output / errors for given testcases on a server.
Integerated with a Telegram Bot ( give code to the Bot and get output ).

Runs multiple threads at a time, executing a program on each thread ensuring optimal usage of CPU and fair CPU division among users.

Constraints maintained : 
- FIFO execution order is maintained at the user level.
- Fair division of CPU time is given to each user.
<br>

System Architecture : 

![first](https://github.com/sacihn173/Code-Runner/assets/73626851/7f3fbdb9-516a-4ee7-91d6-0b6b56eed0fe)


Components : 

1. JobScheduler - Starts a Thread Pool on application start. Maintains a TaskQueue in which Jobs are pushed and picked by the threads to execute.

2. JobExecutor - Recieves the Jobs and executes them by creating a Process of the given program and updates the Job Context with the outputs and errors generated.

3. JobContextHandler - Reflects the latest state of any Job and maintains the complete Job Data.

4. JobQueues - Contains the Jobs for all the user maintaining the FIFO Order.

5. UserQueues - Maintains queue for Users with Jobs in the system. Uses Round Robin method to pick up next user for which to execute job ensuring fair CPU time division among users.


<br>
Application Flow : 

- Jobs are accepted using REST APIs.
- Every job coming for a user is assigned a Job Id. The Job is pushed in the JobQueues which maintains all the jobs for all users in FIFO Order at user level.
- The user is pushed into user queue if not present.
- Using InspectJob API, the status of the job can be fetched.

- The Scheduler starts once the application is started and creates a Thread Pool and a TaskQueue from where the thread picks up Jobs.
- A seperate thread is started whose job is to pick jobs from JobQueues in a Round Robin manner using the UserQueue and JobQueues.
- Once Job is picked by the thread, it is assigned to the Executor and it is executed.
- The Job Context Handler reflects the latests Job state at all the times.
- Once Job is executed, response can be fetched through InspectJob API.
<br>

Operations Time Complexity : 
<br>
- Job Queue Insertion : O(1)
- Job Pickup for Execution implementing Round Robin : O(1)



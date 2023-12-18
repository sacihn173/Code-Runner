# Code-Runner

Application to run code and get output / errors for given testcases on a server.
Integerated with a Telegram Bot ( give code to the Bot and get output ).

Runs multiple threads at a time, executing a program on each thread ensuring optimal usage of CPU and fair CPU division among users.

Constraints maintained : 
- No two threads execute program from same user at the same time.
- FIFO execution order is maintained at the user level.
- Fair division of CPU time is given to each user.
<br>

System Architecture : 

![first](https://github.com/sacihn173/Code-Runner/assets/73626851/7f3fbdb9-516a-4ee7-91d6-0b6b56eed0fe)


Components : 

1. Scheduler - Starts a Thread Pool on application start. Maintains a TaskQueue in which Jobs are pushed and picked by the threads to execute.

2. Executor - Recieves the Jobs and executes them by creating a Process of the given program and updates the Job Context with the outputs and errors generated.

3. Job Context Handler - Reflects the latest state of any Job and maintains the complete Job Data.

4. JobQueues - Contains the Jobs for all the user maintaining the FIFO Order.

5. UserQueues - Maintains queue for Users with Jobs in the system. Uses Round Robin method to pick up next user for which to execute job ensuring fair CPU time division among users.

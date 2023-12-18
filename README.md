# Code-Runner

Application to run code and get output / errors for given testcases on a server.
Integerated with a Telegram Bot ( give code to the Bot and get output ).

Runs multiple threads at a time, executing a program on each thread ensuring optimal usage of CPU and fair CPU division among users.

Constraints maintained : 
- No two threads execute program from same user at the same time.
- FIFO execution order is maintained at the user level.
- Fair division of CPU time is given to each user.


System Architecture : 

![first](https://github.com/sacihn173/Code-Runner/assets/73626851/7f3fbdb9-516a-4ee7-91d6-0b6b56eed0fe)


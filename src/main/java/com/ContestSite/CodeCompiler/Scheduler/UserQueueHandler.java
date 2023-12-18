package com.ContestSite.CodeCompiler.Scheduler;

public class UserQueueHandler {

    // Username of the user is used as Key
    private static UserQueue<String> queue;

    static {
        queue = new UserQueue<>();
    }

    public static void push(String username) {
        queue.push(username);
    }

    public static String pop() {
        return queue.pop();
    }

}

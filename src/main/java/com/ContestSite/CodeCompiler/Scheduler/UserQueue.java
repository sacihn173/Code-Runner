package com.ContestSite.CodeCompiler.Scheduler;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class UserQueue <T> {

    private Queue<T> queue;

    private Set<T> users;

    public UserQueue() {
        queue = new LinkedBlockingQueue<>();
        users = new HashSet<>();
    }

    public synchronized void push(T user) {
        if(!users.contains(user)) {
            queue.add(user);
            users.add(user);
        }
    }

    public synchronized T pop() {
        if(queue.isEmpty()) {
            return null;
        }
        T user = queue.peek();
        queue.poll();
        users.remove(user);
        return user;
    }

}

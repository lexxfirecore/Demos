package com.edifecs.hibertest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by c-ionnmoro on 16-Nov-16.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Started.");
        List<GenericRunner> runners = createRunners();
        Runner runner = new Runner(runners);
        runner.configure();
        runner.run();
    }

    private static List<GenericRunner> createRunners() {
        List<GenericRunner> runners = new LinkedList<GenericRunner>();
        //runners.add(createSessionFactoryRunner());
        runners.add(new TestHbmFactoryRunner());
        return runners;
    }

    private static GenericRunner createSessionFactoryRunner() {
        GenericRunner runner = new SessionFactoryRunner();
        return runner;
    }
}

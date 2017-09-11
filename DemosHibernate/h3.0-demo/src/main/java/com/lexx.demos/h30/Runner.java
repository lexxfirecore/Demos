package com.edifecs.hibertest;

import java.util.List;

/**
 * Created by c-ionnmoro on 16-Nov-16.
 */
public class Runner extends GenericRunner {
    private List<GenericRunner> runners;

    public Runner(List<GenericRunner> runners) {
        this.runners = runners;
    }

    @Override
    public void configure() throws Exception {
        for (GenericRunner runner : runners)
            runner.configure();
    }

    @Override
    public void run() {
        for (GenericRunner runner : runners)
            runner.run();
    }
}

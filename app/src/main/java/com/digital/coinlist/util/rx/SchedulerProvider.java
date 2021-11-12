package com.digital.coinlist.util.rx;


import io.reactivex.rxjava3.core.Scheduler;

public interface SchedulerProvider {

  Scheduler ui();

  Scheduler computation();

  Scheduler io();

}

package com.dpzain.breaker.task;

public interface VoidTask extends Task<Void> {

  @Override
  default Void execute() throws Exception {
    executeVoid();
    return null;
  }

  void executeVoid() throws Exception;
  
}

package net.socialgamer.cah.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class SafeTimerTask implements Runnable {
  private static final Logger LOG = LogManager.getLogger(SafeTimerTask.class);

  @Override
  public final void run() {
    try {
      process();
    } catch (Exception ex) {
      LOG.error("Exception running SafeTimerTask", ex);
    }
  }

  public abstract void process();

}

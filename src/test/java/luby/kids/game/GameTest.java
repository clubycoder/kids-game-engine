package luby.kids.game;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameTest extends Game {
    private static final Logger logger = Logger.getLogger(GameTest.class.getName());

    final Object stopMutex = new Object();

    public GameTest(String name) {
        super(name, 1280, 720);
    }

    @Override
    public void start() {
        super.start();
        synchronized (stopMutex) {
            logger.log(Level.INFO, "Waiting for game to stop or 5 seconds");
            try {
                stopMutex.wait(5000);
            } catch (InterruptedException e) {
                logger.log(Level.INFO, "Interupted by ", e);
            }
            logger.log(Level.INFO, "All done");
        }
    }

    @Override
    public void stop() {
        super.stop();
        logger.log(Level.INFO, "Game stopped");
        synchronized (stopMutex) {
            logger.log(Level.INFO, "Notifying runner game is stopped");
            stopMutex.notifyAll();
        }
    }
}

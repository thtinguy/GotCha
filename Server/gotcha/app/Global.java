
import play.*;

/*
 * Global object that defines some of the applications global settings at 
 * start and stop.
 */
public class Global extends GlobalSettings {

    /*
     * (non-Javadoc)
     * @see play.GlobalSettings#onStart(play.Application)
     */
    public void onStart(Application app) {
        Logger.info("Launch has been a success, all engines engage, next stop Mars");
    }

    /*
     * (non-Javadoc)
     * @see play.GlobalSettings#onStop(play.Application)
     */
    public void onStop(Application app) {
        Logger.info("Coming back to earth");
    }

}
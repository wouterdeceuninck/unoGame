package dispatcher.infoObjects;

import applicationServer.ServerInterface;

public class ApplicationServerInfo {
    public final ServerInterface serverInterface;
    public final int portnumber;

    public ApplicationServerInfo (int portnumber, ServerInterface serverInterface) {
        this.portnumber = portnumber;
        this.serverInterface = serverInterface;
    }
}

package matlab;

import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabOperations;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
/*
public class Connect extends Thread {
    MatlabOperations proxy;
    public Connect(MatlabOperations proxy) {
        this.proxy = proxy;
    }

    public void run() {
        MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
                .setHidden(true)
                .setUsePreviouslyControlledSession(true)
                .setMatlabLocation(null)
                .build();

        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        try
        {
            proxy = factory.getProxy();
        }
        catch (MatlabConnectionException e)
        {
            e.printStackTrace();
        }
    }
}
*/
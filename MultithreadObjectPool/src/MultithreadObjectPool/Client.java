package MultithreadObjectPool;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {



    public void run(){

        try {

            ConnectionPool pool = ConnectionPool.GetPoolInstance();
            pool.setPool();
            Connection obj = pool.acquireObject();
            pool.releaseObject(obj);

        } catch (InterruptedException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {

        for(int i = 0; i < 15 ; i++){
            new Client().start();
        }


    }
}

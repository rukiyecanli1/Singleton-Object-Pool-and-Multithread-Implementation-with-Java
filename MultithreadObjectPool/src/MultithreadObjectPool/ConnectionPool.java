package MultithreadObjectPool;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//import static javafx.scene.input.KeyCode.T;

/**
 *
 * @author rukiye
 */
public class ConnectionPool {

    // yapıcı ile nesne örneklenmesi engelleniyor
    private ConnectionPool() {
    }

    private ArrayList<Connection> _available = new ArrayList<>();
    //private static Object lockObject = new Object();
    private int MAXTotalObjects = 10;
    private boolean isFull = false;

    private static Lock lock = new ReentrantLock();

    // volatile ile derleyicin bu nesneyi optimizasyonu engelleniyor
    // nesne her defasında önbellekten değil de ram'den okunacak
    private static volatile ConnectionPool instance = null;

    public static ConnectionPool GetPoolInstance() {
        lock.lock();      // thread'ler eş zamanlı olarak çalışamayacak
        try {
            if (instance == null) {
                instance = new ConnectionPool();
            }
        }  finally {
            lock.unlock();
        }

        return instance;
    }


    public synchronized void setPool(){

        if( !isFull )
        for(int i = 1; i <= MAXTotalObjects ; i++){
            System.out.print(i+".");
            Connection obj = new Connection();
            _available.add(obj);
        }
        isFull = true;
    }


    public synchronized Connection acquireObject() throws InterruptedException {

            if (_available.isEmpty()) {
              //  System.out.println("havuz boş, bağlantı nesnelerinin bırakılması bekleniyor...");
                try {
                    System.out.println("havuzdaki nesne sayısı: "+_available.size());
                    System.out.println("("+Thread.currentThread().getName() +")"+
                            " öğrenci bekliyor...");
                    wait();
                    System.out.println("havuzdaki nesne sayısı: "+_available.size());
                   // System.out.println(Thread.currentThread().getName() + " devam ediyor...");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Connection obj = _available.get(0);
            _available.remove(obj);

            System.out.println("("+Thread.currentThread().getName()+")"+/*" "  + obj +*/
                    " öğrenci ders seçimi yapıyor... ");
            Thread.sleep(4000);
            return obj;

        }

    public synchronized void  releaseObject(Connection obj) throws InterruptedException
    {

            System.out.println("("+Thread.currentThread().getName()+")"+/*" "  + obj +*/
                    " öğrenci çıkış yapıyor... ");
            Thread.sleep(1000);
            _available.add(obj);
             notifyAll();

    }







}

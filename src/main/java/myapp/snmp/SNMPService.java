package myapp.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Victor Vlad Corcodel on 24/05/2017.
 */
public class SNMPService {

    static ExecutorService executorService = Executors.newFixedThreadPool(15);

    static Address targetAddress = null;
    static CommunityTarget target = new CommunityTarget();

    static {
        try {
            targetAddress = new UdpAddress(InetAddress.getByName("demo.snmplabs.com"), 161);
        } catch (Exception e) {
        }
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setVersion(SnmpConstants.version2c);
    }

    public static List<String> getPduResults(List<String> oids) {
        List<String> results = new ArrayList<>();
        List<PDU> list = new ArrayList<>();

        for (String oid : oids) {
            PDU pdu = getPDU(oid);
            list.add(pdu);
        }

        List<Future<String>> futureResults = new ArrayList<>();

        list.stream().forEach(pdu -> {futureResults.add(executorService.submit(new ParallelPDU(pdu)));});

        futureResults.stream().forEach(future -> {
                    try {
                        results.add(future.get());
                    } catch (Exception e) {
                        System.out.println();
                    }
                }
        );

        executorService.shutdown();
//        long start = System.currentTimeMillis();
//        for (PDU p : list) {
//            performCall(p);
//        }
//        long stop = System.currentTimeMillis();
//        System.out.println(stop-start);
//
//        System.out.println("\n"+"Threads time:");
//        List<Runnable> runnableList = new ArrayList<>();
//        for (PDU p : list) {
//            runnableList.add(new ParallelPDU(p));
//        }
//        long start1 = System.currentTimeMillis();
//        runnableList.forEach(t -> new Thread(t).start());
//        long stop1 = System.currentTimeMillis();
//        System.out.println(stop1-start1);
        return results;
    }

    public static PDU getPDU(String oid) {
        PDU requestPDU = new PDU();
        requestPDU.add(new VariableBinding(new OID(oid)));
        requestPDU.setType(PDU.GET);
        return requestPDU;
    }

    public static String performCall(PDU pdu) {
        // SNMP object and listen all connections for the response
        Snmp snmp = null;

        try {
            snmp = new Snmp(new DefaultUdpTransportMapping());
            snmp.listen();

            ResponseEvent response = snmp.send(pdu, target);
            if (response.getResponse() == null) {
                // request timed out
            } else {
//                System.out.println("Received response from: " +
//                        response.getPeerAddress() + response.getResponse().toString());
                return response.getResponse().toString();
            }
        } catch (Exception e) {

        }
        return null;
    }

}

class ParallelPDU implements Callable<String> {

    PDU pdu;

    public ParallelPDU(PDU pdu) {
        this.pdu = pdu;
    }

    @Override
    public String call() throws Exception {
        return SNMPService.performCall(pdu);
    }

}

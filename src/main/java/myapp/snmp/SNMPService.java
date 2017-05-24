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

/**
 * Created by Victor Vlad Corcodel on 24/05/2017.
 */
public class SNMPService {

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

    public static void main(String[] args) {
        List<PDU> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PDU pdu = getPDU("1.3.6.1.2.1.1.1.0");
            list.add(pdu);
        }

        long start = System.currentTimeMillis();
        for (PDU p : list) {
            performCall(p);
        }
        long stop = System.currentTimeMillis();
        System.out.println(stop-start);

        System.out.println("\n"+"Threads time:");
        List<Runnable> runnableList = new ArrayList<>();
        for (PDU p : list) {
            runnableList.add(new ParallelPDU(p));
        }
        long start1 = System.currentTimeMillis();
        runnableList.forEach(t -> new Thread(t).start());
        long stop1 = System.currentTimeMillis();
        System.out.println(stop1-start1);


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
                System.out.println("Received response from: " +
                        response.getPeerAddress());
                // dump response PDU
                System.out.println(response.getResponse().toString());
                return response.toString();
            }
        } catch (Exception e) {

        }
        return null;
    }

}

class ParallelPDU implements Runnable {

    PDU pdu;

    public ParallelPDU(PDU pdu) {
        this.pdu = pdu;
    }

    @Override
    public void run() {
        SNMPService.performCall(this.pdu);
    }
}

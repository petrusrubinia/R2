package pizzashop.repository;


import org.apache.log4j.Logger;

import pizzashop.model.Payment;
import pizzashop.model.PaymentType;


import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PaymentRepository implements IPaymentRepository {
    static Logger log = Logger.getLogger(PaymentRepository.class.getName());
    private static String filename;
    private List<Payment> paymentList;

    public PaymentRepository(String filename){
        this.filename = filename;
        this.paymentList = new ArrayList<>();
        readPayments();
    }
    public PaymentRepository(String filename, ArrayList list){
        this.filename = filename;
        this.paymentList = list;
        readPayments();
    }

    public void readPayments(){
        ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                paymentList.add(payment);
            }
            br.close();
        } catch (FileNotFoundException e) {
            log.debug("File not Found error");
        } catch (IOException e) {
            log.debug("IOException error");
        }if (br != null) {
            // again, a resource is involved, so try-catch another time
            try {
                br.close();
            } catch (IOException e) {
                log.debug("IOException error ");
            }
        }
    }

    public Payment getPayment(String line){
        Payment item=null;
        if (line==null|| line.equals("")) return null;
        StringTokenizer st=new StringTokenizer(line, ",");
        int tableNumber= Integer.parseInt(st.nextToken());
        String type= st.nextToken();
        double amount = Double.parseDouble(st.nextToken());
        item = new Payment(tableNumber, PaymentType.valueOf(type), amount);
        return item;
    }

    public void add(Payment payment){
        paymentList.add(payment);
        writeAll();
    }

    public List<Payment> getAll(){
        return paymentList;
    }

    public void writeAll(){
        ClassLoader classLoader = PaymentRepository.class.getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
                for (Payment p:paymentList) {
                log.debug(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            log.debug("IoException error");
        }if (bw != null) {
            // again, a resource is involved, so try-catch another time
            try {
                bw.close();
            } catch (IOException e) {
                log.debug("IoException error");
            }
        }
    }

}

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Project4 {
  public static void main(String[] args) {
    try (Socket socket = new Socket("18.221.102.182", 38004)) {
      OutputStream os = socket.getOutputStream();
      InputStream is = socket.getInputStream();
      InputStreamReader isr = new InputStreamReader(is, "UTF-8");
      BufferedReader br = new BufferedReader(isr);

      String version = "0110"; //version 6
      String trafficClass = "00000000"; //don't implement
      String flowLabel = "00000000000000000000"; //don't implement
      String nextHeader = "00010001"; //17
      String hopLimit = "00010100"; //20

      String sAdd = "00000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111100010010110111010110011010110110";
      String dAdd = "00000000000000000000000000000000000000000000000000000000000000000000000000000000111111111111111100110111010101111001011100010110";

      for (int i = 1; i <= 12; i++) {
        int dataLength = (int) Math.pow(2, i);
        System.out.printf("Data length: %d\n", dataLength);
        byte[] data = new byte[dataLength];
        String payloadLength = Integer.toBinaryString(dataLength + 20);
        while (payloadLength.length() != 16)
          payloadLength = "0" + payloadLength;
        String headerString = (version + trafficClass + flowLabel + payloadLength + nextHeader + hopLimit + sAdd + dAdd);
        if (headerString.length() != 320)
          throw new Exception("Header is not predicted 320 bits long");
        byte[] header = new byte[40];
        for (int j = 0; j < 40; j++)
          header[j] = (byte) Integer.parseInt(headerString.substring(j * 8, (j + 1) * 8), 2);
        os.write(header);
        System.out.print("Response: 0x");
        for (int j = 0; j < 4; j++)
          System.out.printf("%02x", (br.read() & 0xff));
        System.out.println();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
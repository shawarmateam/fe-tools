import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Kernel {
    public static void main(String[] args) {
        System.out.println(args.length);
        ArrayList<Byte> byteArr = new ArrayList<>();

        if (args.length != 1) System.exit(-1);
        try (FileInputStream fis = new FileInputStream(args[0])) {
            int byteData;
            while ((byteData = fis.read()) != -1) {
                char character = (char) byteData;
                byteArr.add((byte) character);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean stdWriteEnabled = false;
        for (int i=0; i<byteArr.size(); ++i) {
            byte currByte = byteArr.get(i);
            if (stdWriteEnabled && byteArr.get(i) != 0x01) { System.out.print((char)currByte); continue; }

            if (byteArr.get(i) == 0x01) {
                if (byteArr.get(i+2) == 0x10) {
                    stdWriteEnabled = !(byteArr.get(i+1) == 0x00);
                }
            }
        }
    }
}


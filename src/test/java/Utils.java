import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Utils {

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        try (InputStream source = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource)) {
            if (source == null) {
                throw new IOException("Resource not found: " + resource);
            }

            buffer = ByteBuffer.allocateDirect(bufferSize);

            byte[] tempBuffer = new byte[8192];
            while (true) {
                int bytes = source.read(tempBuffer, 0, tempBuffer.length);
                if (bytes == -1) {
                    break;
                }
                buffer.put(tempBuffer, 0, bytes);
            }

            buffer.flip();
        }

        return buffer;
    }
}

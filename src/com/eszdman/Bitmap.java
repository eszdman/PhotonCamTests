package com.eszdman;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Bitmap {
    public enum Config {
        ALPHA_8     (1),
        RGB_565     (3),
        @Deprecated
        ARGB_4444   (4),
        ARGB_8888   (5),
        RGBA_F16    (6),
        HARDWARE    (7);
        final int nativeInt;
        private static Config sConfigs[] = {
                null, ALPHA_8, null, RGB_565, ARGB_4444, ARGB_8888, RGBA_F16, HARDWARE
        };
        Config(int ni) {
            this.nativeInt = ni;
        }
        static Config nativeToConfig(int ni) {
            return sConfigs[ni];
        }
    }
    public enum CompressFormat {
        JPEG          (0),
        PNG           (1),
        @Deprecated
        WEBP          (2),
        WEBP_LOSSY    (3),
        WEBP_LOSSLESS (4);
        CompressFormat(int nativeInt) {
            this.nativeInt = nativeInt;
        }
        final int nativeInt;
    }
    public void copyPixelsFromBuffer(ByteBuffer byteBuffer) {

    }
    public static Bitmap createBitmap(int width, int height, Config config) {
        return new Bitmap();
    }
    public boolean compress(CompressFormat format, int quality, OutputStream stream) {
        if (stream == null) {
            throw new NullPointerException();
        }
        if (quality < 0 || quality > 100) {
            throw new IllegalArgumentException("quality must be 0..100");
        }
        return true;
    }
}

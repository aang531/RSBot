package Util;

public class CameraFunc extends AangUtil {
    private static CameraFunc ourInstance = new CameraFunc();

    public static CameraFunc getInstance() {
        return ourInstance;
    }

    public boolean pitchedUp(){
        return ctx.camera.pitch() == 100;
    }

    public void pitchUp(){
        ctx.camera.pitch(true);
    }
}

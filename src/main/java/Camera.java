import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    public Transform transform;
    private Vector3f pos;
    private Matrix4f projection;
    private float zPos = 0;

    public Camera(Transform transform) {
        this.transform = transform;
        pos = new Vector3f(-transform.getX(),-transform.getY(),0);
        projection = new Matrix4f().setOrtho2D(-transform.sizeX /2,transform.sizeX /2,-transform.sizeY /2,transform.sizeY /2);
    }
    public void init() {
        pos = new Vector3f(-transform.getX()*SceneManager.scaleOfCam[0], -transform.getY()*SceneManager.scaleOfCam[0], -zPos);
    }

    public void setZ(float z) {
        pos.z = z;
        zPos = z;
    }

    public Vector3f getPosition() {return pos;}
    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(this.pos);

        target = projection.mul(pos, target);
        return target;
    }
}

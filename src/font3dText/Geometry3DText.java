package font3dText;

import javax.media.j3d.Font3D;
import javax.media.j3d.Text3D;
import javax.vecmath.Point3f;

public class Geometry3DText extends Text3D {
	public Geometry3DText() {
		setDefaultAttributes();
	}
	
	public Geometry3DText(Font3D font3D) {
		super(font3D);
		setDefaultAttributes();
	}
	
	public Geometry3DText(Font3D font3D, java.lang.String string) {
		super(font3D, string);
		setDefaultAttributes();
	}
	
	public Geometry3DText(Font3D font3D, java.lang.String string, Point3f position) {
		super(font3D, string, position);
		setDefaultAttributes();
	}
	
	public Geometry3DText(Font3D font3D, java.lang.String string, Point3f position, int alignment, int path) {
		super(font3D, string, position, alignment, path);
		setDefaultAttributes();
	}
	
	protected void setDefaultAttributes() {
		setCapability(ALLOW_STRING_WRITE);
	}
}
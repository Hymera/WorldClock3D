package geometry;

import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;

import time.SystemDateTime;
import font3dText.Geometry3DText;

public class GeometryFrontTime {
	private Geometry3DText text3D = null;
	
	public GeometryFrontTime() {
	}
	
	public TransformGroup createGeometry(TransformGroup tfg) {
		Font3D f3d = new Font3D(new Font("Dialog", Font.PLAIN, 1), new FontExtrusion());
		text3D = new Geometry3DText(f3d, "00:00:00", new Point3f(0.0f, 0.0f, 0.0f),
				Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
		
		tfg.addChild(new Shape3D(text3D, myAppearance()));
		
		return tfg;
	}
	
	public void update() {
		String timeformat = "HH:mm:ss";
		String timezone = "Europe/Berlin";
		text3D.setString(SystemDateTime.now(timeformat, timezone));
	}
	
	public Appearance myAppearance() {
		Appearance appearance = new Appearance();
		PolygonAttributes polyAttrib = new PolygonAttributes();
		Color3f myColor3f = new Color3f(1.0f, 0.0f, 0.0f); // (R, G, B) <- 0.0 - 1.0
		// Shader festlegen
		ColoringAttributes colorAttrib = new ColoringAttributes(myColor3f,
				ColoringAttributes.NICEST);
		
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		polyAttrib.setCullFace(PolygonAttributes.CULL_NONE); // set render sides of triangles
		
		appearance.setPolygonAttributes(polyAttrib);
		appearance.setColoringAttributes(colorAttrib);
		
		return appearance;
	}
}
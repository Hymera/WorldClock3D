package geometry;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.media.j3d.*;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;

import font3dText.Geometry3DText;
import time.SystemDateTime;
import utility.CustomFont;
import utility.LatLonCalc;
import utility.MapRange;
import utility.ResourceLoader;

public class GeometryTime {
	private Geometry3DText[] text3D = null;
	private String[][] data = null;
	private int cities = 0;
	
	public GeometryTime() {
	}
	
	public TransformGroup createGeometry(TransformGroup tfg) {
		Font3D f3dText = new Font3D(CustomFont.createFont(Font.BOLD, 1), new FontExtrusion());
		Font3D f3dTime = new Font3D(CustomFont.createFont(Font.PLAIN, 2), new FontExtrusion());
		LatLonCalc latLonCalc = new LatLonCalc();
		Transform3D tf3DRotX = new Transform3D();
		Transform3D tf3DScale = new Transform3D();
		Transform3D tf3DTrans = new Transform3D();
		Transform3D transform3D = new Transform3D();
		Vector3d vec3d = null;
		TransformGroup tformGroup = null;
		data = loadCities("data.csv");
		int textCount = cities * 2; // one for the time and one for the name
		int j = 1;
		text3D = new Geometry3DText[textCount];
		
		for (int i = 1; i < data.length; i++) {
			vec3d = latLonCalc.calc(Double.parseDouble(data[i][3]), Double.parseDouble(data[i][4]));
			
			tf3DRotX.rotX(Math.PI/2);
			tf3DScale.setScale(new Vector3d(0.09, 0.09, 0.02));
			tf3DTrans.setTranslation(vec3d);
			
			transform3D.mul(tf3DRotX, tf3DScale); // 1.scale  2.rotX
			transform3D.mul(rotateY(Double.parseDouble(data[i][3]),
							Double.parseDouble(data[i][4])), transform3D); // 1.scale  2.rotX  3.rotY
			transform3D.mul(tf3DTrans, transform3D); // 1.scale  2.rotX  3.rotY  4.trans
			
			text3D[j - 1] = new Geometry3DText(f3dText, data[i][0], new Point3f(0.0f, 0.0f, 0.0f),
					Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT);
			
			text3D[j] = new Geometry3DText(f3dTime, data[i][2], new Point3f(0.0f, -1.7f, 0.0f),
					Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT);
			
			tformGroup = new TransformGroup(transform3D);
			tformGroup.addChild(new Shape3D(text3D[j - 1], myAppearance()));
			tformGroup.addChild(new Shape3D(text3D[j], myAppearance()));
			
			tfg.addChild(tformGroup);
			j += 2;
		}
		
		return tfg;
	}
	
	public void update() {
		int j = 1;
		String timeformat = "";
		String timezone = "";
		
		for (int i = 1; i < data.length; i++) {
			timeformat = data[i][1];
			timezone = data[i][2];
			text3D[j].setString(SystemDateTime.now(timeformat, timezone));
			
			j += 2;
		}
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
	
	public String[][] loadCities (String filename) {
		String path = "/data/" + filename;
		String line = "";
		String delimiter = ";";
		BufferedReader reader = null;
		int i = 0;
		int rows = 0;
		
		// read csv file count entries
		try {
			reader = new BufferedReader(new InputStreamReader(ResourceLoader.load(path), "UTF-8"));
			
			 while ((line = reader.readLine()) != null) {
				 rows++;
			 }
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		cities = rows - 1; // need the count for my Geometry3DText array | - header
		String[][] data = new String[rows][];
		
		// read csv file save data
		try {
			reader = new BufferedReader(new InputStreamReader(ResourceLoader.load(path), "UTF-8"));
			
			 while ((line = reader.readLine()) != null) {
				 data[i] = line.split(delimiter);
				 i++;
			 }
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return data;
	}
	
	public Transform3D rotateY(double lat, double lon) {
		Transform3D tf3D1 = new Transform3D();
		Transform3D tf3D2 = new Transform3D();		
		double rotate = MapRange.map(lon, -180, 180, -Math.PI, Math.PI);
		
		AxisAngle4d t = new AxisAngle4d(0.0, 0.0, 1.0, rotate); // align z axis to longitude
		tf3D1.setRotation(t);
		tf3D2.rotZ(-Math.PI/2); // rotate -90°
		tf3D1.mul(tf3D2, tf3D1);
		
		return tf3D1;
	}
}
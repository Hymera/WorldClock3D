package geometry;

import java.awt.Color;
import java.io.IOException;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

import utility.LoadImage;
import utility.MapRange;

public class GeometrySphere {
	private int[][][] rgbList = null;
	private int total = 250; // (10-300) | 10 = min res. and 300 = max res.
	
	public GeometrySphere() {
	}
	
	public Geometry createGeometry() {
		int master = 0;
		double shrink = 100.0;
		double radius = 200.0;
		double landLevel = 1.02;
		double lon = 0.0;
		double lat = 0.0;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;
		Color landColor = Color.BLACK;
		Color oceanColor = Color.WHITE;
		Point3d[][] globe = new Point3d[total + 1][total + 1];
		int[] stripVertexCounts = new int[total];
		
		try {
			LoadImage image = new LoadImage(total, total);
			rgbList = image.getRGBVectorList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// set stripVertexCount
		for (int i = 0; i < total; i++) {
			stripVertexCounts[i] = total * 4;
		}
		
		// set latitude and longitude
		for (int i = 0; i < total + 1; i++) {
			lat = MapRange.map(i, 0, total, 0, Math.PI); // Radians (rad)
			
			for (int j = 0; j < total + 1; j++) {
				lon = MapRange.map(j, 0, total, 0, Math.PI * 2); // Radians (rad)
				
				x = radius * Math.sin(lat) * Math.cos(lon);	// X
				x /= shrink;
				y = radius * Math.sin(lat) * Math.sin(lon);	// Y
				y /= shrink;
				z = radius * Math.cos(lat);	// Z
				z /= shrink;
//				System.out.println("x: " + x + " y: " + y + " z: " + z);
				
				Point3d point = new Point3d(x , y, z);
				// Modify the standard sphere shape to look more like earth
				if (i < total && j < total) {
					int yHelp = i;
					int xHelp = j;
					
					if(rgbList[yHelp][xHelp][1] == 0 && rgbList[yHelp][xHelp][2] == 0 && rgbList[yHelp][xHelp][3] == 0) {	// RGB (0,0,0) = black
						Vector3d vecHelp = new Vector3d(x, y, z);
						vecHelp.scale(landLevel);
						
						point = new Point3d(vecHelp.x, vecHelp.y, vecHelp.z);
					}
				}
				
				globe[i][j] = point;
			}
		}
		
		TriangleStripArray triangleArray = new TriangleStripArray(total * total * 4, GeometryArray.COORDINATES|GeometryArray.NORMALS|GeometryArray.COLOR_3, stripVertexCounts);
		
		for (int i = 0; i < total; i++) {
			for (int j = 0; j < total; j++) {
				triangleArray.setCoordinate(master, globe[i][j]);
				if (isLand(i, j) == true) {
					triangleArray.setColor(master, new Color3f(landColor));
				} else {
					triangleArray.setColor(master, new Color3f(oceanColor));
				}
				master++;
				
				triangleArray.setCoordinate(master, globe[i + 1][j]);
				if (isLand(i + 1, j) == true) {
					triangleArray.setColor(master, new Color3f(landColor));
				} else {
					triangleArray.setColor(master, new Color3f(oceanColor));
				}
				master++;
				
				triangleArray.setCoordinate(master, globe[i][j + 1]);
				if (isLand(i, j + 1) == true) {
					triangleArray.setColor(master, new Color3f(landColor));
				} else {
					triangleArray.setColor(master, new Color3f(oceanColor));
				}
				master++;
				
				triangleArray.setCoordinate(master, globe[i + 1][j + 1]);
				if (isLand(i + 1, j + 1) == true) {
					triangleArray.setColor(master, new Color3f(landColor));
				} else {
					triangleArray.setColor(master, new Color3f(oceanColor));
				}
				master++;
			}
		}
		
		// add a normal map for light reflections
		GeometryInfo geometryInfo = new GeometryInfo(triangleArray);
		NormalGenerator normalGenerator = new NormalGenerator();
		normalGenerator.setCreaseAngle(0.0);
		normalGenerator.generateNormals(geometryInfo);
		GeometryArray geometryArray = geometryInfo.getGeometryArray();
		
		return geometryArray;
	}
	
	// check for land
	public boolean isLand(int i, int j) {
		boolean land = false;
		
		if (i < total && j < total) {
			int yHelp = i;
			int xHelp = j;
			
			if(rgbList[yHelp][xHelp][1] == 0 && rgbList[yHelp][xHelp][2] == 0 && rgbList[yHelp][xHelp][3] == 0) { // RGB (0,0,0) = black
				land = ! land;
			}
		}
		
		return land;
	}
}
package utility;

import javax.vecmath.Vector3d;

public class LatLonCalc {
	private double x = 0.0;
	private double y = 0.0;
	private double z = 0.0;
	private double lon = 0.0;
	private double lat = 0.0;
	private double shrink = 100.0;
	private double radius = 220.0;	// +20 for some space between earth and label

	public LatLonCalc() {
	}

	public Vector3d calc(double lat, double lon) {
		// convert from degrees (°) to radians (rad)
		lat = MapRange.map(lat, 90, -90, 0, Math.PI);
		lon = MapRange.map(lon, -180, 180, 0, Math.PI * 2);
				
		x = radius * Math.sin(lat) * Math.cos(lon); // X
		x /= shrink;
		y = radius * Math.sin(lat) * Math.sin(lon); // Y
		y /= shrink;
		z = radius * Math.cos(lat); // Z
		z /= shrink;

		return new Vector3d(x, y, z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public double getShrink() {
		return shrink;
	}

	public double getRadius() {
		return radius;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setShrink(double shrink) {
		this.shrink = shrink;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
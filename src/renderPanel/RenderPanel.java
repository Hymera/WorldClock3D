package renderPanel;

import geometry.GeometrySphere;
import geometry.GeometryTime;
import io.keyboard.KeyInput;
import io.mouse.MouseInput;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.media.j3d.*;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

import behavior.FpsBehavior;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel {
	private FpsBehavior fpsBehavior = null;
	private Bounds applicationBounds = null;
	private PanelFrontText frontText = null;
	private JLayeredPane lpane = null;
	private boolean pressed = false;
	private Point pointOld = null;
	private int delay = 0;
	private boolean showFPS = false;
	
	private GeometryTime geoTime = null;
	private boolean enableSpin = true;
	private RotationInterpolator rotator = null;
	
	private TransformGroup tfg = null;
	private Transform3D t3d = null;
	private Transform3D t3dstep = new Transform3D();
	private Transform3D t3dReset = null;
	private Matrix4d matrix = new Matrix4d();
	
	public RenderPanel() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
		
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(gc);
		frontText = new PanelFrontText(784, 562);
		lpane = new JLayeredPane();
		
		add("Center", lpane);
		add(frontText);
		add(canvas3D);
		
		canvas3D.addKeyListener(new KeyInput(this));
		MouseInput mouseInput = new MouseInput(this);
		canvas3D.addMouseListener(mouseInput);
		canvas3D.addMouseMotionListener(mouseInput);
		canvas3D.requestFocus();
		BranchGroup scene = createSceneGraph();
		scene.compile();
		
		// SimpleUniverse is a Convenience Utility class
		SimpleUniverse universe = new SimpleUniverse(canvas3D);
		
		// This moves the ViewPlatform back a bit so the
		// objects in the scene can be viewed.
		universe.getViewingPlatform().setNominalViewingTransform();
		
		// camera movement x + y and zoom
		OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.DISABLE_ROTATE|OrbitBehavior.REVERSE_TRANSLATE);
		orbit.setSchedulingBounds(new BoundingSphere());
		universe.getViewingPlatform().setViewPlatformBehavior(orbit);
		
		universe.addBranchGraph(scene);
	}

	public BranchGroup createSceneGraph() {
		BranchGroup group = new BranchGroup();
		tfg = new TransformGroup();
		t3dReset = new Transform3D();
		t3d = new Transform3D();
		tfg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
				
		// time sphere
		TransformGroup tfgTime = new TransformGroup();
		geoTime = new GeometryTime();
		tfgTime = geoTime.createGeometry(tfgTime);
		// sphere earth
		TransformGroup tfgSphere = new TransformGroup();
//		Sphere sphere2 = new Sphere(0.5f, myAppearance());	// library sphere for test
		GeometrySphere geoVerticesSphere = new GeometrySphere();
		Shape3D sphere = new Shape3D(geoVerticesSphere.createGeometry(), myAppearance()); // my own sphere
		tfgSphere.addChild(sphere);
		
		// rotation
		tfgTime = rotatObj(tfgTime);
		tfgSphere = rotatObj(tfgSphere);
		
		// add both TransformGroups
		tfg.addChild(tfgTime);
		tfg.addChild(tfgSphere);
		
		// spin
		tfg = spinObj(tfg);
		
		// position
		tfg = positionObj(tfg);
		
		// save my transformation for later resets
		tfg.getTransform(t3dReset);
		
		// add lights
		TransformGroup tfgLights = new TransformGroup();
		tfgLights = addLights(tfgLights);
		group.addChild(tfgLights);
				
		// add keyPressed
		group.addChild(tfg);
		
		// add fps counter
		fpsBehavior = new FpsBehavior();
		fpsBehavior.setSchedulingBounds(getApplicationBounds());
		group.addChild(fpsBehavior);
		
//		group.addChild(sphere2); // library sphere for test
		
		return group;
	}
	
	public TransformGroup rotatObj(TransformGroup transfg) {
		Transform3D rotation = new Transform3D();	
		rotation.rotX(-Math.PI / 2);
		
		TransformGroup sphereTransformGroup = new TransformGroup(rotation);
		sphereTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTransformGroup.addChild(transfg);
		return sphereTransformGroup;
	}
	
	public TransformGroup spinObj(TransformGroup transfg) {
		TransformGroup sphereTransformGroup = new TransformGroup();
		sphereTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTransformGroup.addChild(transfg);
		
		Alpha rotationAlpha = new Alpha(-1, 60000); // (rotations -1 = infinity, 10.000ms for one rotations or 10sec)
		
		rotator = new RotationInterpolator(rotationAlpha, sphereTransformGroup);
		rotator.setEnable(enableSpin);	// enable or disable auto rotation
		rotator.setSchedulingBounds(getApplicationBounds());
		sphereTransformGroup.addChild(rotator);
		
		return sphereTransformGroup;
	}
	
	public TransformGroup positionObj(TransformGroup transfg) {
		Transform3D transform3D = new Transform3D();
		transform3D.setTranslation(new Vector3d(0.0, 0.0, -7.5)); // position my 3D shape in space
		TransformGroup sphereTransformGroup = new TransformGroup(transform3D);
		sphereTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		sphereTransformGroup.addChild(transfg);
		return sphereTransformGroup;
	}
	
	public TransformGroup addLights(TransformGroup transfg) {
		//Set up the ambient light
		// Create light that shines for 100m from the origin
		Color3f ambientColour = new Color3f(1.0f, 1.0f, 1.0f);
		AmbientLight ambientLight = new AmbientLight(ambientColour);
		ambientLight.setInfluencingBounds(getApplicationBounds());
		
		//Set up the directional light
		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir = new Vector3f(0.0f, -0.5f, -1.0f);
		DirectionalLight light = new DirectionalLight(lightColour, lightDir);
		light.setInfluencingBounds(getApplicationBounds());
		
		//Add the lights to the BranchGroup
		transfg.addChild(ambientLight);
		transfg.addChild(light);
		
		return transfg;
	}
	
	public Appearance myAppearance() {
		// Appearence - Objekt erzeugen
		Appearance appearance = new Appearance();
		// Attribut-Objekt erzeugen
		PolygonAttributes polyAttrib = new PolygonAttributes();
		// Farbobjekt erzeugen mit RGB Farben
		Color3f myColor3f = new Color3f(0.1f, 0.1f, 0.1f); // (R, G, B) <- 0.0 - 1.0
		// material
		Material material = new Material();
		material.setLightingEnable(true);
		material.setAmbientColor(myColor3f);
		
		// Shader festlegen
		ColoringAttributes colorAttrib = new ColoringAttributes(myColor3f,
				ColoringAttributes.NICEST|ColoringAttributes.SHADE_GOURAUD);
		
		// point stroke
		PointAttributes pointAttrib = new PointAttributes();
		pointAttrib.setPointSize(1.0f);
		
		// Attribut-Objekt mit set-Methoden belegen
		// (here polygonmode fill, line, point)
//		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_POINT);
//		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
		polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
		polyAttrib.setCullFace(PolygonAttributes.CULL_BACK); // set render sides of triangles
		
		appearance.setPointAttributes(pointAttrib);
		appearance.setPolygonAttributes(polyAttrib);
		
		appearance.setColoringAttributes(colorAttrib);
		appearance.setMaterial(material);
		
		return appearance;
	}
	
	protected Bounds getApplicationBounds() {
		if (applicationBounds == null)
			applicationBounds = createApplicationBounds();
			
		return applicationBounds;
	}
	
	protected Bounds createApplicationBounds() {
		applicationBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		
		return applicationBounds;
	}
	
	// keyEvent for Keyboard input
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_P) {
			this.enableSpin = !enableSpin;
			rotator.setEnable(enableSpin);
		}
		
		if (key == KeyEvent.VK_R) {
			tfg.setTransform(t3dReset);
		}
		
		if (key == KeyEvent.VK_F) {
			showFPS = !showFPS;
			fpsBehavior.setShowFPS(showFPS);
		}
		
		if (key == KeyEvent.VK_LEFT) {
			t3dstep.rotY(-Math.PI / 64);
			tfg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tfg.setTransform(t3d);
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			t3dstep.rotY(Math.PI / 64);
			tfg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tfg.setTransform(t3d);
		}
		
		if (key == KeyEvent.VK_UP) {
			t3dstep.rotX(-Math.PI / 64);
			tfg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tfg.setTransform(t3d);
		}
		
		if (key == KeyEvent.VK_DOWN) {
			t3dstep.rotX(Math.PI / 64);
			tfg.getTransform(t3d);
			t3d.get(matrix);
			t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
			t3d.mul(t3dstep);
			t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
			tfg.setTransform(t3d);
		}
	}
	
	// MouseEvent for mouse input
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			pressed = true;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		
		if (button == MouseEvent.BUTTON1) {
			pressed = false;
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (pressed == true) {
			Point point = e.getPoint();
			delay++;
			
			if (pointOld != null && delay > 1) {	
				if (point.getX() < pointOld.getX()) {
					t3dstep.rotY(-Math.PI / 64);
					tfg.getTransform(t3d);
					t3d.get(matrix);
					t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
					t3d.mul(t3dstep);
					t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
					tfg.setTransform(t3d);
				}
				
				if (point.getX() > pointOld.getX()) {
					t3dstep.rotY(Math.PI / 64);
					tfg.getTransform(t3d);
					t3d.get(matrix);
					t3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
					t3d.mul(t3dstep);
					t3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
					tfg.setTransform(t3d);
				}
				
				delay = 0;
			}
			
			pointOld = point;
		}
	}
	
	public void run() {
		geoTime.update();
		frontText.update(getWidth(), getHeight());
		frontText.repaint();
	}
}
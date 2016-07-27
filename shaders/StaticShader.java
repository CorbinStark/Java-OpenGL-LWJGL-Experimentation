package shaders;

import org.joml.Matrix4f;

import graphics.Camera;
import util.Maths;

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_tranformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		
	}

	protected void getAllUniformLocations() {
		location_tranformation_matrix = super.getUniformLocation("transformationMatrix");
		location_projection_matrix = super.getUniformLocation("projectionMatrix");
		location_view_matrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_tranformation_matrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(location_projection_matrix, projection);
	}
	
	public void loadViewMatrix(Camera cam) {
		Matrix4f viewMatrix = Maths.createViewMatrix(cam);
		super.loadMatrix(location_view_matrix, viewMatrix);
	}

}
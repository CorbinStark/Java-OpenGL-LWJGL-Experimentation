package shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import graphics.Camera;
import graphics.LightSource;
import graphics.Model;
import util.Maths;

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader.txt";
	
	private int location_tranformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_pos;
	private int location_light_color;
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_use_fake_lighting;
	private int location_sky_color;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	protected void getAllUniformLocations() {
		location_tranformation_matrix = super.getUniformLocation("transformationMatrix");
		location_projection_matrix = super.getUniformLocation("projectionMatrix");
		location_view_matrix = super.getUniformLocation("viewMatrix");
		location_light_pos = super.getUniformLocation("lightPosition");
		location_light_color = super.getUniformLocation("lightColor");
		location_shine_damper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_use_fake_lighting = super.getUniformLocation("useFakeLighting");
		location_sky_color = super.getUniformLocation("skyColor");
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_sky_color, new Vector3f(r,g,b));
	}
	
	public void loadFakeLighting(boolean useFake) {
		super.loadBoolean(location_use_fake_lighting, useFake);
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(location_shine_damper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadLight(LightSource light) {
		super.loadVector(location_light_pos, light.getPosition());
		super.loadVector(location_light_color, light.getColor());
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
	
	public void prepareForRender(Model model) {
		GL11.glClearStencil(0);
		
		if(model.getTexture().isTransparent()) {
			GL11.glDisable(GL11.GL_CULL_FACE);
		} else {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		}
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(model.getPosition(), model.getRotation(), model.getScale());
		loadTransformationMatrix(transformationMatrix);
		
		loadShineVariables(model.getTexture().getShineDamper(), model.getTexture().getReflectivity());
		loadFakeLighting(model.getTexture().hasFakeLighting());
		loadSkyColor(0.2f, 0.8f, 0.9f);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		if(model.getTexture() != null) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
		}
		
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glColor3i(0, 0, 0);
		
		GL11.glStencilFunc(GL11.GL_ALWAYS, 1, -1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL11.glStencilFunc(GL11.GL_NOTEQUAL, 1, -1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		GL11.glLineWidth(4);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}

}
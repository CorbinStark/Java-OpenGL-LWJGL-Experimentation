package shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import graphics.Camera;
import graphics.LightSource;
import graphics.Model;
import util.Maths;

public class TerrainShader extends Shader {
	private static final String VERTEX_FILE = "src/shaders/terrainVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/shaders/terrainFragmentShader.txt";
	
	private int location_tranformation_matrix;
	private int location_projection_matrix;
	private int location_view_matrix;
	private int location_light_pos;
	private int location_light_color;
	private int location_shine_damper;
	private int location_reflectivity;
	private int location_sky_color;
	private int location_background_texture;
	private int location_rTexture;
	private int location_gTexture;
	private int location_bTexture;
	private int location_blendmap;

	public TerrainShader() {
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
		location_sky_color = super.getUniformLocation("skyColor");
		location_background_texture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendmap = super.getUniformLocation("blendmap");
	}
	
	public void loadTextures() {
		super.loadInt(location_background_texture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendmap, 4);
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(location_sky_color, new Vector3f(r,g,b));
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
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(model.getPosition(), model.getRotation(), model.getScale());
		loadTransformationMatrix(transformationMatrix);
		if(model.getTexture() != null) {
			loadShineVariables(model.getTexture().getShineDamper(), model.getTexture().getReflectivity());
		}
		loadSkyColor(0.2f, 0.8f, 0.9f);
		if(model.getTexture() != null) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture(0).getID());
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture(1).getID());
			GL13.glActiveTexture(GL13.GL_TEXTURE2);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture(2).getID());
			GL13.glActiveTexture(GL13.GL_TEXTURE3);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture(3).getID());
			GL13.glActiveTexture(GL13.GL_TEXTURE4);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture(4).getID());
		}
		loadTextures();
	}
}
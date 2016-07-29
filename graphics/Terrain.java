package graphics;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.joml.Vector3f;

import textures.ModelTexture;

public class Terrain {

	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	private static final float MAX_HEIGHT = 80;
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	private float x;
	private float z;
	private Model model;
	private ModelTexture background;
	private ModelTexture rTexture;
	private ModelTexture gTexture;
	private ModelTexture bTexture;
	private ModelTexture blendmap;
	
	public Terrain(int gridX, int gridZ, String heightmap, ModelTexture background, ModelTexture rTexture, ModelTexture gTexture, ModelTexture bTexture, ModelTexture blendmap) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.background = background;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
		this.blendmap = blendmap;
		generate(heightmap);
	}
	
	private void generate(String heightmap){
		
		BufferedImage image = null;
		try{
			image = ImageIO.read(new File("res/" + heightmap + ".png"));
		} catch(Exception e) {
			
		}
		int VERTEX_COUNT = image.getHeight();
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = getHeight(j, i, image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		this.model = new Model(vertices, textureCoords, normals, indices, background, rTexture, gTexture, bTexture, blendmap);
		this.model.setPosition(new Vector3f(x, 0, z));
		this.model.setRotation(new Vector3f(0, 0, 0));
		this.model.setScale(1);
	}
	
	private Vector3f calculateNormal(int x, int y, BufferedImage image) {
		float heightL = getHeight(x-1, y, image);
		float heightR = getHeight(x+1, y, image);
		float heightD = getHeight(x, y-1, image);
		float heightU = getHeight(x, y+1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2f, heightD - heightU);
		normal.normalize();
		return normal;
	}
	
	private float getHeight(int x, int y, BufferedImage image) {
		if(x < 0 || x > image.getHeight()-1 || y < 0 || y >= image.getHeight()-1) {
			return 0;
		}
		float height = image.getRGB(x, y);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}
	
	public void cleanup() {
		model.cleanup();
	}
	
	public Model getModel() {
		return model;
	}

	public ModelTexture getBackground() {
		return background;
	}

	public void setBackground(ModelTexture background) {
		this.background = background;
	}

	public ModelTexture getrTexture() {
		return rTexture;
	}

	public void setrTexture(ModelTexture rTexture) {
		this.rTexture = rTexture;
	}

	public ModelTexture getgTexture() {
		return gTexture;
	}

	public void setgTexture(ModelTexture gTexture) {
		this.gTexture = gTexture;
	}

	public ModelTexture getbTexture() {
		return bTexture;
	}

	public void setbTexture(ModelTexture bTexture) {
		this.bTexture = bTexture;
	}

	public ModelTexture getBlendmap() {
		return blendmap;
	}

	public void setBlendmap(ModelTexture blendmap) {
		this.blendmap = blendmap;
	}

}

package graphics;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import textures.ModelTexture;

public class Image {

	private ModelTexture texture;
	private double width, height;
	private Vector3f position;
	private Vector2f size;
	private Vector4f color;
	
	
	
	public Image(String path) {
		texture = new ModelTexture(path);
		width = texture.getWidth();
		height = texture.getHeight();
		
		float[] vertices = {
				0, 0, 0,
				0, position.y, 0,
				position.x, position.y, 0,
				position.x, 0, 0
		};
		float[] textureCoords = {
				
		};
	}

}

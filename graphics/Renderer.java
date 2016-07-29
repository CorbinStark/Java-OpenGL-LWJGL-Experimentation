package graphics;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import shaders.Shader;

public class Renderer {

	private List<Model> renderQueue = new ArrayList<Model>();
	
	public Renderer() {
		
	}
	
	public void draw(Model model, Shader shader) {
			
		model.bind();
		
		shader.start();
		shader.prepareForRender(model);
			
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
		shader.stop();
		model.unbind();
	}

}

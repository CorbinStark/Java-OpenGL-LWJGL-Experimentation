package graphics;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import shaders.StaticShader;
import util.Maths;

public class BatchRenderer {

	private List<Model> renderQueue = new ArrayList<Model>();
	
	private static final int MAX_SPRITES = 10000;
	private static final int MAX_VERTICES = MAX_SPRITES * 3;
	
	
	public BatchRenderer() {
		
	}
	
	public void submit(Model model) {
		renderQueue.add(model);
	}
	
	public void flush(StaticShader shader) {
		for(int i = 0; i < renderQueue.size(); i++) {
			Model rendermodel = renderQueue.get(i);
			
			rendermodel.bind();
			
			Matrix4f transformationMatrix = Maths.createTransformationMatrix(rendermodel.getPosition(), rendermodel.getRotation(), rendermodel.getScale());
			shader.loadTransformationMatrix(transformationMatrix);
			shader.loadShineVariables(rendermodel.getTexture().getShineDamper(), rendermodel.getTexture().getReflectivity());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, rendermodel.getTexture().getID());
			GL11.glDrawElements(GL11.GL_TRIANGLES, rendermodel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			
			rendermodel.unbind();
			renderQueue.remove(rendermodel);
		}
	}

}

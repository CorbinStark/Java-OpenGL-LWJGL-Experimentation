package shaders;

import graphics.Model;

public class WaterShader extends Shader {

	private final static String VERTEX_FILE = "src/shaders/waterVert.txt";
	private final static String FRAGMENT_FILE = "src/shaders/waterFrag.txt";
	
	public WaterShader(){
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void prepareForRender(Model model) {
		
	}

	protected void getAllUniformLocations() {
		
	}

	protected void bindAttributes() {
		
	}

}
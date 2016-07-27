package entities;

import org.joml.Vector3f;

import graphics.Model;
import shaders.Shader;
import shaders.StaticShader;

public class Entity {

	private Model model;
	
	public Entity(Model model, Vector3f position, Vector3f rotation, float scale) {
		super();
		this.model = model;
		model.setPosition(position);
		model.setRotation(rotation);
		model.setScale(scale);
	}
	
	public void draw(StaticShader shader) {
		model.draw(shader);
	}
	
	public void update() {
		
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		model.increasePosition(dx, dy, dz);
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		model.increaseRotation(dx, dy, dz);
	}
	
	public void cleanup() {
		model.cleanup();
	}
	
}

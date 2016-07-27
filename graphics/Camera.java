package graphics;

import org.joml.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public boolean up, left, right, down, forward, back;
	
	public Camera() {
		
	}

	public void update() {
		if(forward) position.z-=0.02f;
		if(back) position.z+=0.02f;
		if(up) position.y+=0.02f;
		if(down) position.y-=0.02f;
		if(right) position.x+=0.02f;
		if(left) position.x-=0.02f;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}
	
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}

}

package graphics;

import org.joml.Vector3f;

import util.RenderUtils;

public class Camera {

	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	private float distance;
	private float angle = 90;
	
	public boolean up, left, right, down, forward, back;
	
	public Camera() {
		
	}

	public void update() {
		//if(forward) position.z-=0.6f;
		//if(back) position.z+=0.6f;
		if(up) position.y+=0.6f;
		if(down) position.y-=0.6f;
		//if(right) position.x+=0.6f;
		//if(left) position.x-=0.6f;
		if(back) {
			position.z += (float)Math.cos(yaw * RenderUtils.PI / 180);
			position.x -= (float)Math.sin(yaw * RenderUtils.PI / 180);
			position.y += (float)Math.sin(pitch * RenderUtils.PI / 180);
		}
		else if(forward) {
			position.z -= (float)Math.cos(yaw * RenderUtils.PI / 180);
			position.x += (float)Math.sin(yaw * RenderUtils.PI / 180);
			position.y -= (float)Math.sin(pitch * RenderUtils.PI / 180);
		}
		if(right) {
			position.x += (float)Math.cos(yaw * RenderUtils.PI / 180);
			position.z += (float)Math.sin(yaw * RenderUtils.PI / 180);
		}
		else if(left) {
			position.x -= (float)Math.cos(yaw * RenderUtils.PI / 180);
			position.z -= (float)Math.sin(yaw * RenderUtils.PI / 180);
		}
		float a = (float) (distance * Math.cos(Math.toRadians(pitch)));
		float b = (float) (distance * Math.sin(Math.toRadians(pitch)));
		float theta = angle;
		yaw = 180 - angle;
	}
	
	public void calculateZoom(float mousewheel) {
		float zoomLevel = mousewheel * 0.2f;
		distance -= zoomLevel;
	}
	
	public void calculatePitch(float mousey) {
		float pitchChange = mousey * 0.2f;
		pitch -= pitchChange;
	}
	
	public void calculateAngle(float mousex) {
		float angleChange = mousex * 0.2f;
		angle += angleChange;
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

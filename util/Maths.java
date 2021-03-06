package util;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import graphics.Camera;
import mesmerizing.Main;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix);
		matrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix);
		matrix.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix);
		matrix.scale(new Vector3f(scale, scale, scale));
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera cam) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(cam.getPitch()), new Vector3f(1, 0, 0), viewMatrix);
		viewMatrix.rotate((float) Math.toRadians(cam.getYaw()), new Vector3f(0, 1, 0), viewMatrix);
		viewMatrix.rotate((float) Math.toRadians(cam.getRoll()), new Vector3f(0, 0, 1), viewMatrix);
		Vector3f cameraPos = cam.getPosition();
		Vector3f negativePos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		viewMatrix.translate(negativePos);
		return viewMatrix;
	}

}
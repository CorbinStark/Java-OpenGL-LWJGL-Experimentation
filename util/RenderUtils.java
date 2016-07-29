package util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;


public class RenderUtils {

	public static final float PI = 3.141596f;
	
	public static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer putMatrixInBuffer(Matrix4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.m00).put(matrix.m10).put(matrix.m20).put(matrix.m30);
		buffer.put(matrix.m01).put(matrix.m11).put(matrix.m21).put(matrix.m31);
		buffer.put(matrix.m02).put(matrix.m12).put(matrix.m22).put(matrix.m32);
		buffer.put(matrix.m03).put(matrix.m13).put(matrix.m23).put(matrix.m33);
		return buffer;
	}

}

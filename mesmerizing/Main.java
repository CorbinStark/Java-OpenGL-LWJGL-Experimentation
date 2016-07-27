package mesmerizing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import entities.Entity;
import graphics.Camera;
import graphics.Model;
import shaders.StaticShader;
import textures.ModelTexture;


public class Main {
	
	public static final float FOV = 80;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000.0f;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static Matrix4f PROJECTION;
	
	Model model;
	Entity entity;
	ModelTexture texture;
	StaticShader shader;
	static Camera cam = new Camera();
	
	private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
	
	private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
            if(action == GLFW_PRESS) {
            	if(key == GLFW_KEY_RIGHT || key == GLFW_KEY_D) {
            		cam.right = true;
            	}
            	if(key == GLFW_KEY_LEFT || key == GLFW_KEY_A) {
            		cam.left = true;
            	}
            	if(key == GLFW_KEY_UP || key == GLFW_KEY_W) {
            		cam.forward = true;
            	}
            	if(key == GLFW_KEY_DOWN || key == GLFW_KEY_S) {
            		cam.back = true;
            	}
            	if(key == GLFW_KEY_SPACE) {
            		cam.up = true;
            	}
            	if(key == GLFW_KEY_LEFT_CONTROL) {
            		cam.down = true;
            	}
            } else if(action == GLFW_RELEASE) {
            	if(key == GLFW_KEY_RIGHT || key == GLFW_KEY_D) {
            		cam.right = false;
            	}
            	if(key == GLFW_KEY_LEFT || key == GLFW_KEY_A) {
            		cam.left = false;
            	}
            	if(key == GLFW_KEY_UP || key == GLFW_KEY_W) {
            		cam.forward = false;
            	}
            	if(key == GLFW_KEY_DOWN || key == GLFW_KEY_S) {
            		cam.back = false;
            	}
            	if(key == GLFW_KEY_SPACE) {
            		cam.up = false;
            	}
            	if(key == GLFW_KEY_LEFT_CONTROL) {
            		cam.down = false;
            	}
            }
        }
    };
    
    public Main() {
 	long window;
    	
    	glfwSetErrorCallback(errorCallback);
    	if(glfwInit() != true) {
    		throw new IllegalStateException("Unable to initialize");
    	}
    	
    	window = glfwCreateWindow(WIDTH, HEIGHT, "CAN'T. LOOK. AWAY!", NULL, NULL);
    	if(window == NULL) {
    		glfwTerminate();
    		throw new RuntimeException("Couldn't create window");
    	}
    	
    	GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    	glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
    	glfwMakeContextCurrent(window);
    	GL.createCapabilities();

    	shader = new StaticShader();
    	createProjectionMatrix();
    	shader.start();
    	shader.loadProjectionMatrix(PROJECTION);
    	shader.stop();
    	
    	System.err.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        System.err.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        System.err.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
        System.err.println("LWJGL_VERSION: " + org.lwjgl.Version.getVersion());
        System.err.println("OS: " + System.getProperty("os.name"));
        System.err.println("Available Processors:" + java.lang.Runtime.getRuntime().availableProcessors());
        System.err.println("Total Memory: " + java.lang.Runtime.getRuntime().totalMemory());
        
    	glfwWindowHint(GLFW_SAMPLES, 8); //8x anti-aliasing
    	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
    	glfwSwapInterval(1);
    	glfwSetKeyCallback(window, keyCallback);
    	IntBuffer width = BufferUtils.createIntBuffer(1);
    	IntBuffer height = BufferUtils.createIntBuffer(1);
    			
    	texture = new ModelTexture("stallTexture");
		model = new Model("stall", texture);
		entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), .1f);
    	
		GL11.glEnable(GL11.GL_DEPTH_TEST);
    	while(glfwWindowShouldClose(window) != true) {
    		float ratio;
    		shader.start();
    		shader.loadViewMatrix(cam);
    		
    		glfwGetFramebufferSize(window, width, height);
    		ratio = width.get() / (float) height.get();
    		width.rewind();
    		height.rewind();
    		glViewport(0, 0, width.get(), height.get());
    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    		glMatrixMode(GL_PROJECTION_MATRIX);
    		
    		//glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
    		glOrtho(0, 0, WIDTH, HEIGHT, 1, -1);
    		
    		draw(shader);
    		update();

            glfwSwapBuffers(window);
            glfwPollEvents();
            
            shader.stop();

            width.flip();
            height.flip();
    		
    	}
    	
    	glfwDestroyWindow(window);
    	keyCallback.free();
    
    	cleanup();
    	glfwTerminate();
    }
    
    public static void main(String[] args) {
    	Main main = new Main();
    }
    
    private void draw(StaticShader shader) {
    	entity.draw(shader);
    }
    
    private void update() {
    	cam.update();
    	//entity.increasePosition(0.003f, 0, -0.008f);
    	entity.increaseRotation(0, 1, 0);
    }
    
    private void cleanup() {
    	shader.cleanup();
    	entity.cleanup();
    }
    
    private void createProjectionMatrix() {
    	float aspectRatio = WIDTH / HEIGHT;
    	float y_scale = (float)((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
    	float x_scale = y_scale / aspectRatio;
    	float frustrum_length = FAR_PLANE - NEAR_PLANE;
    	
    	PROJECTION = new Matrix4f();
    	PROJECTION.m00 = x_scale;
    	PROJECTION.m11 = y_scale;
    	PROJECTION.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
    	PROJECTION.m23 = -1;
    	PROJECTION.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
    	PROJECTION.m33 = 0;
    }

}

package mesmerizing;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import entities.Entity;
import graphics.Camera;
import graphics.LightSource;
import graphics.Model;
import graphics.Renderer;
import graphics.Terrain;
import shaders.StaticShader;
import shaders.TerrainShader;
import textures.ModelTexture;


public class Main {
	
	public static final float FOV = 90;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000.0f;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private static double mousex;
	private static double mousey;
	private static boolean leftmouse = false;
	private static boolean buttonchange = false;
	
	public static Matrix4f PROJECTION;
	
	Model model;
	Entity entity;
	ModelTexture texture;
	StaticShader shader;
	TerrainShader terrainShader;
	
	Terrain terrain;
	Terrain terrain2;
	Terrain terrain3;
	Terrain terrain4;
	
	LightSource light = new LightSource(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));
	static Camera cam = new Camera();
	private Renderer renderer = new Renderer();
	
	private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
	
	private static GLFWMouseButtonCallback mouseCallback = new GLFWMouseButtonCallback() {
		public void invoke(long window, int button, int action, int mods) {
			if(action == GLFW_PRESS) {
				if(button == GLFW_MOUSE_BUTTON_LEFT) {
					leftmouse = true;
					buttonchange = true;
				}
			} else 
				if(action == GLFW_RELEASE) {
					if(button == GLFW_MOUSE_BUTTON_LEFT) {
						leftmouse = false;
					}
				}
		}
	};
	
	private static GLFWCursorPosCallback cursorCallback = new GLFWCursorPosCallback() {
		public void invoke(long window, double xpos, double ypos) {
			if(buttonchange) {
				mousex = xpos;
				mousey = ypos;
				buttonchange = false;
			}
			float xDiff = (float)(mousex - xpos);
			float yDiff = (float) (mousey - ypos);
			if(leftmouse) {
				mousex = xpos;
				mousey = ypos;
				cam.calculateAngle(xDiff);
				cam.calculatePitch(yDiff);
			}
		}
	};
	
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
 	
	glfwWindowHint(GLFW_SAMPLES, 8); //8x anti-aliasing
	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
	glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    	
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
    	terrainShader = new TerrainShader();
    	createProjectionMatrix();
    	shader.start();
    	shader.loadProjectionMatrix(PROJECTION);
    	shader.stop();
    	terrainShader.start();
    	terrainShader.loadProjectionMatrix(PROJECTION);
    	terrainShader.stop();
    	
    	System.err.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        System.err.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        System.err.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
        System.err.println("LWJGL_VERSION: " + org.lwjgl.Version.getVersion());
        System.err.println("OS: " + System.getProperty("os.name"));
        System.err.println("Available Processors:" + java.lang.Runtime.getRuntime().availableProcessors());
        System.err.println("Total Memory: " + java.lang.Runtime.getRuntime().totalMemory());
        
    	glfwSwapInterval(1);
    	//glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
    	glfwSetKeyCallback(window, keyCallback);
    	glfwSetMouseButtonCallback(window, mouseCallback);
    	glfwSetCursorPosCallback(window, cursorCallback);
    	IntBuffer width = BufferUtils.createIntBuffer(1);
    	IntBuffer height = BufferUtils.createIntBuffer(1);
    			
    	texture = new ModelTexture("white");
    	texture.setShineDamper(10);
    	texture.setReflectivity(1f);
		model = new Model("dragon", texture);
		entity = new Entity(model, new Vector3f(0, 0, -5), new Vector3f(0, 0, 0), 1f);
		terrain = new Terrain(-1, -1, "heightmap", new ModelTexture("testgrass"), new ModelTexture("dirt"), new ModelTexture("grass2"), new ModelTexture("cobblestone"), new ModelTexture("blendmap"));
		terrain2 = new Terrain(1, -1, "heightmap", new ModelTexture("testgrass"), new ModelTexture("dirt"), new ModelTexture("grass2"), new ModelTexture("cobblestone"), new ModelTexture("blendmap"));
		terrain3 = new Terrain(-1, 1, "heightmap", new ModelTexture("testgrass"), new ModelTexture("dirt"), new ModelTexture("grass2"), new ModelTexture("cobblestone"), new ModelTexture("blendmap"));
		terrain4 = new Terrain(1, 1, "heightmap", new ModelTexture("testgrass"), new ModelTexture("dirt"), new ModelTexture("grass2"), new ModelTexture("cobblestone"), new ModelTexture("blendmap"));
		GL11.glClearColor(0.2f, 0.8f, 0.9f, 1);
    	
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
    	while(glfwWindowShouldClose(window) != true) {
    		float ratio;
    		shader.start();
    		shader.loadViewMatrix(cam);
    		shader.loadLight(light);
    		shader.stop();
    		terrainShader.start();
    		terrainShader.loadViewMatrix(cam);
    		terrainShader.loadLight(light);
    		terrainShader.stop();
    		
    		glfwGetFramebufferSize(window, width, height);
    		ratio = width.get() / (float) height.get();
    		width.rewind();
    		height.rewind();
    		glViewport(0, 0, width.get(), height.get());
    		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    		
    		draw();
    		update();

            glfwSwapBuffers(window);
            glfwPollEvents();
            
            shader.stop();
            terrainShader.stop();

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
    
    private void draw() {
    	renderer.draw(entity.getModel(), shader);
    	renderer.draw(terrain.getModel(), terrainShader);
    	renderer.draw(terrain2.getModel(), terrainShader);
    	renderer.draw(terrain3.getModel(), terrainShader);
    	renderer.draw(terrain4.getModel(), terrainShader);
    }
    
    private void update() {
    	cam.update();
    	//light.setPosition(cam.getPosition());
    	//entity.increasePosition(0.3f, 0, -0.08f);
    	entity.increaseRotation(0, 0.5f, 0);
    }
    
    private void cleanup() {
    	shader.cleanup();
    	terrainShader.cleanup();
    	entity.cleanup();
    	terrain.cleanup();
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

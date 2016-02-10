package com.cout970.editor.util;


public class Camara {

//	public static final Camara INSTANCE = new Camara();
//	private Vector3d position;
//	private Vector3d rotation;
//	private boolean blocked;
//
//	public Camara() {
//		position = new Vector3d(0, 0, 0);
//		rotation = new Vector3d(0, 0, 0);
//	}
//
//	public void update() {
//		VoxelReality.GLFW_HANDLER.updateCursor(blocked);
//
//		rotation.add(GLFWHandler.cursorDifY * 0.15D, GLFWHandler.cursorDifX * 0.15D, 0);
//
//		if (rotation.getX() > 90)
//			rotation.set(90, rotation.getY(), rotation.getZ());
//		if (rotation.getX() < -90)
//			rotation.set(-90, rotation.getY(), rotation.getZ());
//
//		if (rotation.getY() > 360)
//			rotation.add(0, -360, 0);
//		if (rotation.getY() < 0)
//			rotation.add(0, 360, 0);
//
//	}
//
//	public void rotate(double angle, Vector3d axis) {
//		if (!blocked)
//			rotation.add(axis.copy().multiply(angle));
//	}
//
//	public void translate(Vector3d pos) {
//		if (!blocked)
//			position.add(pos);
//	}
//
//	public void reset() {
//		position = new Vector3d(0, 0, 0);
//		rotation = new Vector3d(0, 0, 0);
//	}
//
//	public Vector3d getPosition() {
//		return position.copy();
//	}
//
//	public Vector3d getRotation() {
//		return rotation.copy();
//	}
//
//	public void applyTransformations(IRenderEngine eng) {
//		eng.loadIdentity();
//		eng.rotate(rotation.getX(), 1, 0, 0);
//		eng.rotate(rotation.getY(), 0, 1, 0);
//		eng.rotate(rotation.getZ(), 0, 0, 1);
//		eng.translate(position.getX(), position.getY(), position.getZ());
//	}
//
//	public void setBlocked(boolean b) {
//		blocked = b;
//	}
//
//	public boolean isBlocked() {
//		return blocked;
//	}
//
//	public void printfDebugInfo() {
//		Log.debug("Camera Transformations:");
//		Log.debug("Translation: " + position);
//		Log.debug("Rotation: " + rotation);
//	}
//
//	public void handleKeyboard(int key) {
//		float speed = (float) (8.0f * VoxelReality.getDeltaSec());
//		Vector3d dir;
//
//		switch (key) {
//
//		case GLFW_KEY_W:
//			dir = new Vector3d(0, 0, speed);
//			dir.rotateY(Math.toRadians(-rotation.getY()));
//			translate(dir.set(dir.getX(), 0, dir.getZ()));
//			break;
//
//		case GLFW_KEY_A:
//			dir = new Vector3d(speed, 0, 0);
//			dir.rotateY(Math.toRadians(-rotation.getY()));
//			translate(dir.set(dir.getX(), 0, dir.getZ()));
//			break;
//
//		case GLFW_KEY_S:
//			dir = new Vector3d(0, 0, -speed);
//			dir.rotateY(Math.toRadians(-rotation.getY()));
//			translate(dir.set(dir.getX(), 0, dir.getZ()));
//			break;
//
//		case GLFW_KEY_D:
//			dir = new Vector3d(-speed, 0, 0);
//			dir.rotateY(Math.toRadians(-rotation.getY()));
//			translate(dir.set(dir.getX(), 0, dir.getZ()));
//			break;
//
//		case GLFW_KEY_SPACE:
//			translate(new Vector3d(0, -speed, 0));
//			break;
//
//		case GLFW_KEY_LEFT_SHIFT:
//			translate(new Vector3d(0, speed, 0));
//			break;
//
//		case GLFW_KEY_O:
//			printfDebugInfo();
//			break;
//		}
//	}
}

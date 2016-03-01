package com.cout970.editor.util;

/**
 * Created by cout970 on 11/01/2016.
 */
public class RotationVect {

    private float pitch;
    private float yaw;

    public RotationVect(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public Vect2d toVect2d() {
        return new Vect2d(yaw, pitch);
    }

    public void set(Vect2d rot) {
        yaw = (float) rot.getX();
        pitch = (float) rot.getY();
    }

    public float getYawWrappedTo180() {
        return wrapTo180(yaw);
    }

    public static float wrapTo180(float angle) {
        angle %= 360.0F;

        if (angle >= 180.0F) {
            angle -= 360.0F;
        }

        if (angle < -180.0F) {
            angle += 360.0F;
        }

        return angle;
    }

    public Direction toHorizontalAxis() {
        float yaw = getYawWrappedTo180();
        if ((yaw < 45 && yaw >= 0) || (yaw > -45 && yaw <= 0)) {
            return Direction.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            return Direction.WEST;
        } else if (yaw <= -45 && yaw > -135) {
            return Direction.EAST;
        } else if ((yaw >= 135 && yaw <= 180) || (yaw <= -135 && yaw >= -180)) {
            return Direction.NORTH;
        }
        throw new IllegalStateException("Invalid yaw: " + yaw);
    }
}

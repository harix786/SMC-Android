package rs.pedjaapps.smc.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.*;

import rs.pedjaapps.smc.model.GameObject;
import rs.pedjaapps.smc.model.Maryo;
import rs.pedjaapps.smc.model.World;

public class MarioController
{

    enum Keys
    {
        LEFT, RIGHT, UP, DOWN, JUMP, FIRE
    }

    private static final long LONG_JUMP_PRESS = 150l;
    private static final float MAX_JUMP_SPEED = 7f;
    
    private World world;
    private Maryo maryo;
    private long jumpPressedTime;
    private boolean jumpingPressed;

    static Set<Keys> keys = new HashSet<Keys>();

    public MarioController(World world)
    {
        this.world = world;
        this.maryo = world.getMario();
    }

    // ** Key presses and touches **************** //

    public void leftPressed()
    {
        keys.add(Keys.LEFT);
    }

    public void rightPressed()
    {
        keys.add(Keys.RIGHT);
    }

    public void upPressed()
    {
        keys.add(Keys.UP);
    }

    public void downPressed()
    {
        keys.add(Keys.DOWN);
    }

    public void jumpPressed()
    {
        keys.add(Keys.JUMP);
    }

    public void firePressed()
    {
        keys.add(Keys.FIRE);
    }

    public void leftReleased()
    {
        keys.remove(Keys.LEFT);
    }

    public void rightReleased()
    {
        keys.remove(Keys.RIGHT);
    }

    public void upReleased()
    {
        keys.remove(Keys.UP);
    }

    public void downReleased()
    {
        keys.remove(Keys.DOWN);
    }

    public void jumpReleased()
    {
        keys.remove(Keys.JUMP);
        jumpingPressed = false;
    }

    public void fireReleased()
    {
        keys.remove(Keys.FIRE);
    }

    /**
     * The main update method *
     */
    public void update(float delta)
    {
        //maryo.setGrounded(maryo.getVelocity().y == 0);
        //System.out.println(maryo.getVelocity().y + " : " + maryo.isGrounded());
		if(!maryo.isGrounded())
		{
			maryo.setWorldState(Maryo.WorldState.JUMPING);
		}
        processInput();
        if (maryo.isGrounded() && maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
        {
            maryo.setWorldState(Maryo.WorldState.IDLE);
        }

        maryo.update(delta);
        //world.getLevel().getPb().moveX(delta);
	}

    /**
     * Change Mario's state and parameters based on input controls *
     */
    private boolean processInput()
    {
        Vector3 vel = maryo.getVelocity();
        Vector3 pos = maryo.getPosition();
        if (keys.contains(Keys.JUMP))
        {
            //System.out.println("jump");
            if (!maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                jumpingPressed = true;
                jumpPressedTime = System.currentTimeMillis();
                maryo.setWorldState(Maryo.WorldState.JUMPING);
                //maryo.getVelocity().y = MAX_JUMP_SPEED;
                maryo.setGrounded(false);
            }
            else
            {
                if (jumpingPressed && ((System.currentTimeMillis() - jumpPressedTime) >= LONG_JUMP_PRESS))
                {
                    jumpingPressed = false;
                }
                else
                {
                    if (jumpingPressed && vel.y < MAX_JUMP_SPEED)
                    {
                        //maryo.getVelocity().y = MAX_JUMP_SPEED;
                        //maryo.getBody().setTransform(pos.x, pos.y + 0.01f, 0);
                        maryo.setVelocity(vel.x, vel.y = +11f);
                    }
                }
            }
        }
        if (keys.contains(Keys.LEFT))
        {
            //System.out.println("left");
            // left is pressed
            maryo.setFacingLeft(true);
            if (!maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                maryo.setWorldState(Maryo.WorldState.WALKING);
            }
            //if (vel.x > -MAX_VEL)
            //{
                //maryo.getBody().applyLinearImpulse(-1.2f, 0, 0, 0, true);
                maryo.setVelocity(vel.x = -4f, vel.y);
            //}
        }
        else if (keys.contains(Keys.RIGHT))
        {
            //System.out.println("right");
            // right is pressed
            maryo.setFacingLeft(false);
            if (!maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                maryo.setWorldState(Maryo.WorldState.WALKING);
            }
            //if (vel.x < MAX_VEL)
            //{
                //maryo.getBody().applyLinearImpulse(1.2f, 0, 0, 0, true);
                maryo.setVelocity(vel.x = +4f, vel.y);
            //}
        }
        else if (keys.contains(Keys.DOWN))
        {
            //System.out.println("down");
            if (!maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                maryo.setWorldState(Maryo.WorldState.DUCKING);
            }
        }
        else
        {
            if (!maryo.getWorldState().equals(Maryo.WorldState.JUMPING))
            {
                maryo.setWorldState(Maryo.WorldState.IDLE);
            }
            //slowly decrease linear velocity on x axes
            maryo.setVelocity(vel.x * 0.7f, vel.y);
        }
        return false;
    }

    public void setMaryo(Maryo mario)
    {
        this.maryo = mario;
    }
}

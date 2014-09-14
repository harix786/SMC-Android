package rs.pedjaapps.smc.model.enemy;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

import rs.pedjaapps.smc.Assets;
import rs.pedjaapps.smc.utility.Constants;
import rs.pedjaapps.smc.utility.Utility;

/**
 * Created by pedja on 18.5.14..
 */
public class Flyon extends Enemy
{
    public static final float FLYON_VELOCITY = 3f;
    private boolean goingUp = true, topReached, bottomReached;
    private long maxPositionReachedTs = 0;
    private long minPositionReachedTs = 0;
    private static final long STAY_TOP_TIME = 300;//2 seconds
    private static final long STAY_BOTTOM_TIME = 2500;//3 seconds

    public Flyon(Vector3 position, float width, float height)
    {
        super(position, width, height);
    }

    @Override
    public void loadTextures()
    {
        TextureAtlas atlas = Assets.manager.get(textureAtlas);
        Array<TextureAtlas.AtlasRegion> frames = atlas.getRegions();
        //frames.add(atlas.findRegion(TKey.two.toString()));

        Assets.animations.put(textureAtlas, new Animation(0.25f, frames));
    }

    @Override
    public void render(SpriteBatch spriteBatch)
    {
        TextureRegion frame = Assets.animations.get(textureAtlas).getKeyFrame(stateTime, true);

        //spriteBatch.draw(frame, body.getPosition().x - getBounds().width/2, body.getPosition().y - getBounds().height/2, bounds.width, bounds.height);
        Utility.draw(spriteBatch, frame, position.x - bounds.width / 2, position.y - bounds.height / 2, bounds.height);
    }

    public void update(float deltaTime)
    {
		position.x = 0;
        stateTime += deltaTime;

        long timeNow = System.currentTimeMillis();
        if((topReached && timeNow - maxPositionReachedTs < STAY_TOP_TIME))
        {
            //body.applyForceToCenter(0, /*+world.getGravity().y*/20, true);
            setVelocity(0, 0);
            return;
        }
        else
        {
            if(position.y > 5)
            {
                maxPositionReachedTs = System.currentTimeMillis();
                goingUp = false;
                topReached = true;
            }
            else
            {
                topReached = false;
                maxPositionReachedTs = 0;
            }
        }
        if((bottomReached && timeNow - minPositionReachedTs < STAY_BOTTOM_TIME))
        {
            setVelocity(0, 0);
            return;
        }
        else
        {
            if(position.y <= 1.5f)
            {
                minPositionReachedTs = System.currentTimeMillis();
                goingUp = true;
                bottomReached = true;
            }
            else
            {
                bottomReached = false;
                minPositionReachedTs = 0;
            }
        }
        if(goingUp)
        {
            setVelocity(0, velocity.y =+((Constants.CAMERA_HEIGHT - position.y)/3f));
        }
        else
        {
            //body.setLinearDamping(5);
            setVelocity(0, velocity.y =-((Constants.CAMERA_HEIGHT - position.y)/3f));
        }

    }
}